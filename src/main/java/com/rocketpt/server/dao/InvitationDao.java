package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.InvitationEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author yzr
 */
@Mapper
public interface InvitationDao extends BaseMapper<InvitationEntity> {
}
