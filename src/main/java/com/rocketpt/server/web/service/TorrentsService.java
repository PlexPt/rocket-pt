package com.rocketpt.server.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.base.Res;
import com.rocketpt.server.web.dao.TorrentsDao;
import com.rocketpt.server.web.entity.TorrentsEntity;
import com.rocketpt.server.web.entity.param.TorrentParam;

import org.springframework.stereotype.Service;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
@Service
public class TorrentsService extends ServiceImpl<TorrentsDao, TorrentsEntity> {


    public Res queryPage(TorrentParam params) {

        return Res.ok();
    }
}

