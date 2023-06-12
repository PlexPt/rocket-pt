package com.rocketpt.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.Type;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dao.TorrentDao;
import com.rocketpt.server.dto.TorrentDto;
import com.rocketpt.server.dto.entity.TorrentEntity;
import com.rocketpt.server.dto.param.TorrentAddParam;
import com.rocketpt.server.dto.param.TorrentAuditParam;
import com.rocketpt.server.service.infra.TorrentManager;
import com.rocketpt.server.service.sys.UserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TorrentService extends ServiceImpl<TorrentDao, TorrentEntity> {

    final TorrentManager torrentManager;

    final UserService userService;
    final TorrentStorageService torrentStorageService;
    final Bencode infoBencode = new Bencode(StandardCharsets.ISO_8859_1);

    public void add(TorrentAddParam param) {
        TorrentEntity entity = BeanUtil.copyProperties(param, TorrentEntity.class);
        entity.setStatus(TorrentEntity.Status.CANDIDATE);
        entity.setFileStatus(0);
        entity.setOwner(userService.getUserId());

        //TODO 保证幂等
        save(entity);
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void upload(Integer id, byte[] bytes, String filename) {
        TorrentEntity entity = getById(id);
        if (entity == null) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "该种子不存在");
        }
        if (entity.getFileStatus() == 1) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "该种子已上传过，如需重传，请删除后再试");
        }
        if (!entity.getOwner().equals(userService.getUserId())) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "该种子不属于你");
        }


        byte[] transformedBytes = torrentManager.transform(bytes);
        byte[] infoHash = torrentManager.infoHash(transformedBytes);


        long count = count(Wrappers.<TorrentEntity>lambdaQuery()
                .eq(TorrentEntity::getInfoHash, infoHash));

        if (count != 0) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "该种子站内已存在。");
        }
        TorrentDto dto = torrentManager.parse(bytes);

        entity.setInfoHash(infoHash);
        entity.setFilename(filename);

        entity.setSize(dto.getTorrentSize());
        entity.setFileCount(dto.getTorrentCount().intValue());
        entity.setType(dto.getTorrentCount() > 1 ? 2 : 1);
        updateById(entity);
        torrentStorageService.save(id, transformedBytes);

    }

    public byte[] fetch(Integer torrentId, String passkey) {
        byte[] fileBytes = torrentStorageService.read(torrentId);

        if (fileBytes == null) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage(
                    "torrent_not_exists"));
        }

        Map<String, Object> decodedMap = infoBencode.decode(fileBytes, Type.DICTIONARY);
        if (StringUtils.isEmpty(passkey)) {
            passkey = userService.getPasskey(userService.getUserId());
        }
        //TODO 校验用户下载权限
        decodedMap.put("announce",
                Constants.Announce.PROTOCOL + "://" + Constants.Announce.HOSTNAME + ":" + Constants.Announce.PORT + "/" + passkey);

        return infoBencode.encode(decodedMap);
    }

    public void audit(TorrentAuditParam param) {

        //todo 校验审核权限

        Integer id = param.getId();
        TorrentEntity entity = getById(id);
        if (entity == null) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "该种子不存在");
        }
        if (entity.getStatus() != 0) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "该种子不是待审核状态");
        }
        entity.setReviewer(userService.getUserId());

        entity.setStatus(param.getStatus());
        if (StringUtils.isNotEmpty(param.getRemark())) {
            entity.setRemark(param.getRemark());
        }
        updateById(entity);

    }

    public void update(TorrentEntity entity) {
        Integer id = entity.getId();
        TorrentEntity torrent = getById(id);

        if (torrent == null) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage(
                    "torrent_not_exists"));
        }
        if (!entity.getOwner().equals(userService.getUserId())) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "该种子不属于你");
        }

        torrent.setDescription(entity.getDescription());
        torrent.setTitle(entity.getTitle());
        torrent.setName(entity.getName());
        torrent.setSubheading(entity.getSubheading());
        torrent.setStatus(TorrentEntity.Status.RETRIAL);

        updateById(torrent);
    }

    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            TorrentEntity entity = getById(id);
            if (!entity.getOwner().equals(userService.getUserId())) {
                throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "该种子不属于你");
            }
        }

        removeByIds(Arrays.asList(ids));
    }

    public TorrentEntity getByInfoHash(byte[] infoHash) {
        TorrentEntity entity = getOne(new QueryWrapper<TorrentEntity>()
                        .lambda()
                        .eq(TorrentEntity::getInfoHash, infoHash)
                , false
        );
        return entity;
    }

    /**
     * 收藏或者取消收藏
     */
    public void favorite(Integer torrentId, Integer userId) {
        //todo 收藏或者取消收藏
    }
}

