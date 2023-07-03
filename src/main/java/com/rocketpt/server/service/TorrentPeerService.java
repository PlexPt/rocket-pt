package com.rocketpt.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dao.TorrentPeerDao;
import com.rocketpt.server.dto.entity.TorrentPeerEntity;

import org.springframework.stereotype.Service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 种子Peer表
 *
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-05-31 16:25:59
 */
@Service
@Slf4j
public class TorrentPeerService extends ServiceImpl<TorrentPeerDao, TorrentPeerEntity> {


    /**
     * 从数据库获取peer列表
     *
     * @param torrentId
     * @param seeder
     * @param peerNumWant
     * @return
     */
    public List<TorrentPeerEntity> listByTorrent(Integer torrentId,
                                                 Boolean seeder,
                                                 Integer peerNumWant) {

        List<TorrentPeerEntity> list = list(new QueryWrapper<TorrentPeerEntity>()
                .lambda()
                .eq(TorrentPeerEntity::getTorrentId, torrentId)
        );

        return list;
    }

    public boolean peerExists(Integer userId, Integer torrentId, byte[] peerId) {
        long count = count(new QueryWrapper<TorrentPeerEntity>()
                .lambda()
                .eq(TorrentPeerEntity::getTorrentId, torrentId)
                .eq(TorrentPeerEntity::getUserId, userId)
                .eq(TorrentPeerEntity::getPeerId, peerId)
        );
        return count > 0;
    }

    public void delete(Integer userId, Integer torrentId, byte[] peerId) {
        remove(new QueryWrapper<TorrentPeerEntity>()
                .lambda()
                .eq(TorrentPeerEntity::getTorrentId, torrentId)
                .eq(TorrentPeerEntity::getUserId, userId)
//                .eq(TorrentPeerEntity::getPeerId, peerId)
        );
    }
}

