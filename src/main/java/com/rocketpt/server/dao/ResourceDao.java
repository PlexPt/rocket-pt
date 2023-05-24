package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.ResourceEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author plexpt
 */
@Mapper
public interface ResourceDao extends BaseMapper<ResourceEntity> {

}
