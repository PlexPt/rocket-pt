package com.rocketpt.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.base.Res;
import com.rocketpt.server.common.base.ResPage;
import com.rocketpt.server.dao.TorrentsDao;
import com.rocketpt.server.dto.entity.TorrentsEntity;
import com.rocketpt.server.dto.param.TorrentParam;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
@Service
public class TorrentsService extends ServiceImpl<TorrentsDao, TorrentsEntity> {


    public Res queryPage(TorrentParam params) {
        List<TorrentsEntity> list = list();

        return Res.ok(list, new ResPage(list.size(), 1, 50));
    }
}

