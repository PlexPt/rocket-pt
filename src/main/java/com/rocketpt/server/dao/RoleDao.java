package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.RoleEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author plexpt
 */
@Mapper
public interface RoleDao extends BaseMapper<RoleEntity> {

//    @Select("select distinct u.* FROM user u join user_role r ON r.user_id = u.id where r" +
//            ".role_id=#{roleId}")
//    List<UserEntity> findRoleUsers(Long roleId);

}
