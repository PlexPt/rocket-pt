package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.SysConfigEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置信息表
 *
 * @author plexpt
 */
@Mapper
public interface SysConfigDao extends BaseMapper<SysConfigEntity> {

}
