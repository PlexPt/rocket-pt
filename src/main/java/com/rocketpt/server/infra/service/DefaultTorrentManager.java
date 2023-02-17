package com.rocketpt.server.infra.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.Type;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.SessionItemHolder;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.TorrentDto;
import com.rocketpt.server.dto.entity.TorrentFile;
import com.rocketpt.server.sys.dto.UserinfoDTO;
import com.rocketpt.server.sys.repository.TorrentFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yzr
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultTorrentManager implements TorrentManager {

    private final Bencode bencode = new Bencode(StandardCharsets.UTF_8);
    private final Bencode infoBencode = new Bencode(StandardCharsets.ISO_8859_1);
    private final TorrentFileRepository torrentFileRepository;

    @Override
    public TorrentDto parse(byte[] bytes) {
        Map<String, Object> decodeDict = bencode.decode(bytes, Type.DICTIONARY);
        if (decodeDict.containsKey("piece layers") || decodeDict.containsKey("files tree") ||
                decodeDict.containsKey("meta version") && (int) decodeDict.get("meta version") == 2) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage("protocol_v2_not_support"));
        }
        Map<String, Object> infoDict = (Map<String, Object>) decodeDict.get("info");
        Long size;
        Long count;
        if (infoDict.containsKey("length")) {
            size = (Long) infoDict.get("length");
            count = 1L;
        } else {
            List<Map<String, Object>> files = (List<Map<String, Object>>) infoDict.get("files");
            size = files.stream().map(f -> (Long) f.get("length")).collect(Collectors.summingLong(i -> i));
            count = (long) files.size();
        }
        TorrentDto dto = new TorrentDto();
        dto.setTorrentSize(size);
        dto.setTorrentCount(count);
        dto.setDict(decodeDict);
        return dto;
    }

    @Override
    public byte[] transform(byte[] bytes) {
        Map<String, Object> map = infoBencode.decode(bytes, Type.DICTIONARY);
        map.remove("announce-list");
        map.remove("announce");
        Map<String, Object> infoMap = (Map<String, Object>) map.get("info");
        infoMap.put("private", 1);
        infoMap.put("source", Constants.Source.PREFIX + Constants.Source.NAME);
        map.put("info", infoMap);
        return infoBencode.encode(map);
    }

    @Override
    public byte[] infoHash(byte[] bytes) throws NoSuchAlgorithmException {
        Map<String, Object> decodedMap = infoBencode.decode(bytes, Type.DICTIONARY);
        Map<String, Object> infoDecodedMap = (Map<String, Object>) decodedMap.get("info");
        byte[] encode = infoBencode.encode(infoDecodedMap);
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return md.digest(encode);
    }

    @Override
    public void preserve(Integer torrentId, byte[] bytes, TorrentFile.IdentityType identityType) {
        TorrentFile torrentFile = new TorrentFile();
        torrentFile.setTorrentId(torrentId);
        torrentFile.setFile(bytes);
        torrentFile.setIdentityType(identityType);
        torrentFileRepository.insert(torrentFile);
    }

    @Override
    public byte[] fetch(Integer torrentId) {
        Optional<TorrentFile> fileOptional = Optional.ofNullable(torrentFileRepository.selectOne(
                Wrappers.<TorrentFile>lambdaQuery()
                        .eq(TorrentFile::getTorrentId, torrentId)
        ));
        if (fileOptional.isEmpty()) throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage("torrent_not_exists"));
        Map<String, Object> decodedMap = infoBencode.decode(fileOptional.get().getFile(), Type.DICTIONARY);
        UserinfoDTO dto = (UserinfoDTO) SessionItemHolder.getItem(Constants.SESSION_CURRENT_USER);
        decodedMap.put("announce", Constants.Announce.PROTOCOL + "://" + Constants.Announce.HOSTNAME + ":" + Constants.Announce.PORT + "/" + dto.credential().passkey());
        return infoBencode.encode(decodedMap);
    }
}
