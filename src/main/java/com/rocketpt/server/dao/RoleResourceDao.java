package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.RoleMenuEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author plexpt
 */
@Mapper
public interface RoleResourceDao extends BaseMapper<RoleMenuEntity> {

}
