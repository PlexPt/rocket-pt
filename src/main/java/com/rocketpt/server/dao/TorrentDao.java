package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.TorrentEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
@Mapper
public interface TorrentDao extends BaseMapper<TorrentEntity> {

}
