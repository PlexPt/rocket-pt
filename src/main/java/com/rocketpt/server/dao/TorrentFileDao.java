package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.TorrentFileEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author yzr
 */
@Mapper
public interface TorrentFileDao extends BaseMapper<TorrentFileEntity> {
}
