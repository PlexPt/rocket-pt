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
public interface UserDao extends BaseMapper< UserEntity> {


    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     */
    List<String> queryAllPerms(Integer userId);

    @Select("select * from User where id in (#{userIds})")
    Set<UserEntity> findByIds(Set<Long> userIds);

    Set<String> listUserPermissions(Integer userId);


    @Select("SELECT * FROM t_user u LEFT JOIN t_user_credential uc on uc.id = u.id WHERE uc.passkey=#{passkey}")
    UserEntity findUserByPasskey(String passkey);

}
