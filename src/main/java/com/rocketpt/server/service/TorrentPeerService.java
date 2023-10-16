package com.rocketpt.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dao.TorrentPeerDao;
import com.rocketpt.server.dto.entity.TorrentPeerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
                // 如果下载完了，就不需要获取seeder用户了
                // 如果当前用户是 seeder，那么这段代码将寻找 leecher；如果当前用户不是 seeder（或者不确定是否是 seeder），那么就不对 peer 的类型进行过滤
                .eq(seeder, TorrentPeerEntity::getSeeder, 0)
                .last("order by rand() limit " + peerNumWant)
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

    public TorrentPeerEntity getPeer(Integer userId, Integer torrentId, byte[] peerId) {
        return getOne(new QueryWrapper<TorrentPeerEntity>()
                .lambda()
                .eq(TorrentPeerEntity::getTorrentId, torrentId)
                .eq(TorrentPeerEntity::getUserId, userId)
                .eq(TorrentPeerEntity::getPeerId, peerId), false
        );
    }

    public void delete(Integer userId, Integer torrentId, String peerIdHex) {
        remove(new QueryWrapper<TorrentPeerEntity>()
                .lambda()
                .eq(TorrentPeerEntity::getTorrentId, torrentId)
                .eq(TorrentPeerEntity::getUserId, userId)
                .eq(TorrentPeerEntity::getPeerIdHex, peerIdHex)
        );
    }
}

