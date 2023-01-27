package com.rocketpt.server.sys.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.sys.entity.Resource;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author plexpt
 */
@Mapper
public interface ResourceRepository extends BaseMapper<Resource> {

}
