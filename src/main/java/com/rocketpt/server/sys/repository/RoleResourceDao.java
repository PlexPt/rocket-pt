package com.rocketpt.server.sys.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.sys.entity.RoleResource;
import com.rocketpt.server.sys.entity.UserRole;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author plexpt
 */
@Mapper
public interface RoleResourceDao extends BaseMapper<RoleResource> {

}
