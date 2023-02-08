package com.rocketpt.server.web.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.SessionItemHolder;
import com.rocketpt.server.common.base.Res;
import com.rocketpt.server.common.base.ResPage;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.TorrentDto;
import com.rocketpt.server.dto.entity.TorrentFile;
import com.rocketpt.server.infra.service.TorrentManager;
import com.rocketpt.server.sys.dto.UserinfoDTO;
import com.rocketpt.server.web.dao.TorrentsDao;
import com.rocketpt.server.web.entity.TorrentsEntity;
import com.rocketpt.server.web.entity.param.TorrentParam;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
@Service
public class TorrentsService extends ServiceImpl<TorrentsDao, TorrentsEntity> {

    @Resource
    private TorrentManager torrentManager;

    public Res queryPage(TorrentParam params) {
        List<TorrentsEntity> list = list();

        return Res.ok(list, new ResPage(list.size(), 1, 50));
    }

    @SneakyThrows
    @Transactional(rollbackFor = SQLException.class)
    public Res upload(byte[] bytes, TorrentsEntity torrentsEntity) {
        byte[] transformedBytes = torrentManager.transform(bytes);
        byte[] infoHash = torrentManager.infoHash(transformedBytes);
        if (count(Wrappers.<TorrentsEntity>lambdaQuery().eq(TorrentsEntity::getInfoHash, infoHash)) != 0)
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "该种子站内已存在。");
        TorrentDto dto = torrentManager.parse(bytes);
        Map<String, Object> parsedMap = dto.getDict();
        Map<String, Object> infoParsedMap = (Map<String, Object>) parsedMap.get("info");
        TorrentsEntity entity = new TorrentsEntity();
        BeanUtil.copyProperties(torrentsEntity, entity, "id","infoHash","added","visible","approvalStatus");
        entity.setInfoHash(infoHash);
        String name = (String) infoParsedMap.get("name");
        entity.setName(name);
        entity.setFilename(Constants.Source.PREFIX + name + ".torrent");
        entity.setSize(dto.getTorrentSize());
        entity.setNumfiles(dto.getTorrentCount().intValue());
        entity.setType(dto.getTorrentCount() > 1 ? "multi" : "single");
        UserinfoDTO userinfoDTO = (UserinfoDTO) SessionItemHolder.getItem(Constants.SESSION_CURRENT_USER);
        entity.setOwner(userinfoDTO.userId().intValue());
        entity.setAdded(new Date());
        save(entity);
        torrentManager.preserve(entity.getId(), transformedBytes, TorrentFile.IdentityType.V1);
        //todo get torrent protocol version
        return Res.ok();
    }
}

