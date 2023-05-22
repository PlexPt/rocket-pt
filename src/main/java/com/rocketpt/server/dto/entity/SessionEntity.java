package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rocketpt.server.util.JsonUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 会话
 *
 * @author plexpt
 */
@Data
@TableName("session")
public class SessionEntity extends EntityBase {

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String token;

    private Long credentialId;
    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private LocalDateTime expireTime;
    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private LocalDateTime lastLoginTime;

    private LocalDateTime lastModifiedTime;

    private String data;

    //    @Transient
    @TableField(exist = false)

    private boolean active;


    public static SessionEntity of(Long id, String token, UserCredentialEntity credential,
                                   Serializable data,
                                   LocalDateTime expireTime) {
        SessionEntity authSessionEntity = new SessionEntity();
        authSessionEntity.setId(id);
        authSessionEntity.setToken(token);
        authSessionEntity.setCredentialId(credential.getId());
        authSessionEntity.setExpireTime(expireTime);
        authSessionEntity.setLastLoginTime(LocalDateTime.now());
        authSessionEntity.setActive(true);
        authSessionEntity.setData(JsonUtils.stringify(data));
        return authSessionEntity;
    }


}
