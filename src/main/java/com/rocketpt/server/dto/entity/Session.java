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
public class Session extends EntityBase {

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


    public static Session of(Long id, String token, UserCredential credential, Serializable data,
                             LocalDateTime expireTime) {
        Session authSession = new Session();
        authSession.setId(id);
        authSession.setToken(token);
        authSession.setCredentialId(credential.getId());
        authSession.setExpireTime(expireTime);
        authSession.setLastLoginTime(LocalDateTime.now());
        authSession.setActive(true);
        authSession.setData(JsonUtils.stringify(data));
        return authSession;
    }


}
