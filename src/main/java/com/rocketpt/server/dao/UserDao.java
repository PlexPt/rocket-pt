package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.UserEntity;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author plexpt
 */
@Mapper
public interface UserDao extends BaseMapper<com.rocketpt.server.dto.entity.UserEntity> {


    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     */
    List<String> queryAllPerms(Long userId);

    @Select("select * from User where id in (#{userIds})")
    Set<UserEntity> findByIds(Set<Long> userIds);

    Set<String> listUserPermissions(Long userId);


}
