package com.rocketpt.server.sys.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * @author plexpt
 */
@Mapper
public interface UserRepository extends BaseMapper<User> {

    @Select("select * from User where id in (#{userIds})")
    Set<User> findByIds(Set<Long> userIds);

    Set<String> listUserPermissions(Long userId);

    //or user.organization.parentIds like concat(#{orgParentIds}, '%'))
    @Select("""
            select * from user u where (u.organization_id=#{organizationId}
                        
            and (#{username} is null or u.username=#{username})
            and (#{state} is null or u.state=#{state}))
            """)
    List<User> findOrgUsers(Pageable pageable, String username, User.State state,
                            Long organizationId, String orgParentIds);

    //"or u.organization.parentIds like concat(#{orgParentIds}, '%')"
    @Select("select count(u.id) from user u where u.organization_id=#{organizationId} ")
    long countOrgUsers(Long organizationId, String orgParentIds);
}
