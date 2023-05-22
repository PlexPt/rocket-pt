package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.SessionEntity;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author plexpt
 */
@Mapper
public interface SessionDao extends BaseMapper<SessionEntity> {

    @Transactional(rollbackFor = Exception.class)
    @Delete("delete from session where expire_time <= now() ")
    void deleteExpiredSession();

    @Transactional(rollbackFor = Exception.class)
    @Update("update session set expire_time= #{expireTime}, last_modified_time = now() where " +
            "token = " +
            "#{token}")
    void updateExpireTime(String token, LocalDateTime expireTime);

    @Select("select * from session where token = #{token}")
    SessionEntity findByToken(String token);

}
