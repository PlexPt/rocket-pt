package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.OrganizationEntity;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author plexpt
 */
@Mapper
public interface OrganizationDao extends BaseMapper<OrganizationEntity> {

    @Select("select * from organization org where org.parent_id = #{parentId}")
    List<OrganizationEntity> findByParentId(@Param("parentId") Long parentId);
}
