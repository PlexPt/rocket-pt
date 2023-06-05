package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.TorrentPeerEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 种子Peer
 *
 */
@Mapper
public interface TorrentPeerDao extends BaseMapper<TorrentPeerEntity> {

}
