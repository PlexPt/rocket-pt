package com.rocketpt.server.service.infra;

import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.Type;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dao.TorrentFileDao;
import com.rocketpt.server.dto.TorrentDto;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzr
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultTorrentManager implements TorrentManager {

    private final Bencode bencode = new Bencode(StandardCharsets.UTF_8);
    private final Bencode infoBencode = new Bencode(StandardCharsets.ISO_8859_1);
    private final TorrentFileDao torrentFileDao;

    @Override
    public TorrentDto parse(byte[] bytes) {
        Map<String, Object> decodeDict = bencode.decode(bytes, Type.DICTIONARY);
        if (decodeDict.containsKey("piece layers") || decodeDict.containsKey("files tree") ||
                decodeDict.containsKey("meta version") && (int) decodeDict.get("meta version") == 2) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage(
                    "protocol_v2_not_support"));
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

    /**
     * 修改torrent文件：
     *
     * 修改tracker
     * 修改为私有种子 private=1
     * http://bittorrent.org/beps/bep_0027.html
     *
     * @param bytes
     * @return
     */
    @Override
    public byte[] transform(byte[] bytes) {
        Map<String, Object> map = infoBencode.decode(bytes, Type.DICTIONARY);
        map.remove("announce-list");
        map.remove("announce");
        map.put("comment", "rocket pt");
        Map<String, Object> infoMap = (Map<String, Object>) map.get("info");
        infoMap.put("private", 1);
        //todo 配置
        //todo 配置前缀
        infoMap.put("source", Constants.Source.PREFIX + Constants.Source.NAME);
        map.put("info", infoMap);

        // 屏蔽Bittorrent v2种子上传
        if (map.containsKey("piece layers") || infoMap.containsKey("files tree")
                || (infoMap.containsKey("meta version") && infoMap.get("meta version").equals(2))) {

            throw new RocketPTException("不支持使用 Bittorrent 协议 v2 创建的 Torrent 文件或混合 torrent");
        }



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


}
