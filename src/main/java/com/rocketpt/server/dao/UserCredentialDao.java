package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.UserCredential;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * @author plexpt
 */
@Mapper
public interface UserCredentialDao extends BaseMapper<UserCredential> {

    @Select("select * from user_credential authCredential where authCredential" +
            ".identifier=#{identifier} and " +
            "authCredential.identity_type=#{identityType}")
    Optional<UserCredential> findCredential(String identifier,
                                            UserCredential.IdentityType identityType);

}
