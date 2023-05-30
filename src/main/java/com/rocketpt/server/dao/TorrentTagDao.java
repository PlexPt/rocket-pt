package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.TorrentTagEntity;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TorrentTagDao extends BaseMapper<TorrentTagEntity> {
}
