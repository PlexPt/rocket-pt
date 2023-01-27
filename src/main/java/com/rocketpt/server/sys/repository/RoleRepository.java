package com.rocketpt.server.sys.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.sys.entity.Role;
import com.rocketpt.server.sys.entity.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author plexpt
 */
@Mapper
public interface RoleRepository extends BaseMapper<Role> {

    @Select("select distinct u.* FROM user u join user_role r ON r.user_id = u.id where r.role_id=#{roleId}")
    List<User> findRoleUsers(Long roleId, Pageable pageable);

}
