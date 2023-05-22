package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.UserCredentialEntity;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * @author plexpt
 */
@Mapper
public interface UserCredentialDao extends BaseMapper<UserCredentialEntity> {


}
