package com.rocketpt.server.sys.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.Organization;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author plexpt
 */
@Mapper
public interface OrganizationRepository extends BaseMapper<Organization> {

    @Select("select * from organization org where org.parent_id = #{parentId}")
    List<Organization> findByParentId(@Param("parentId") Long parentId);
}
