package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.util.SecurityUtil;
import com.rocketpt.server.common.exception.RocketPTException;

import java.security.NoSuchAlgorithmException;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * 用户凭证 user_credential
 *
 * @author plexpt
 */
@Data
@NoArgsConstructor
@TableName("user_credential")
public class UserCredential extends EntityBase {

    /**
     * 标识（手机号 邮箱 用户名或第三方应用的唯一标识）
     */
    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String identifier;

    private String userId;
    /**
     * 密码凭证（站内的保存密码，站外的不保存或保存token）
     */
    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String credential;
    /**
     * 登录类型（手机号 邮箱 用户名）或第三方应用名称（微信 微博等）
     */
    private IdentityType identityType;

    public UserCredential(String identifier, String credential, IdentityType identityType) {
        this.identifier = identifier;
        this.credential = credential;
        this.identityType = identityType;
    }

    public boolean doCredentialMatch(String credential) {
        try {
            //TODO 未实现其他登录方式
            if (this.getIdentityType() != IdentityType.PASSWORD || !SecurityUtil.md5(identifier,
                    credential).equals(this.getCredential())) {
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RocketPTException(CommonResultStatus.FAIL, "密码加密失败：" + e.getMessage());
        }
        return true;
    }


    @RequiredArgsConstructor
    public enum IdentityType {
        PASSWORD(0);
        @EnumValue
        //标记数据库存的值是code
        private final int code;

    }

    public enum State {
        NORMAL,
        LOCKED;

    }


}
