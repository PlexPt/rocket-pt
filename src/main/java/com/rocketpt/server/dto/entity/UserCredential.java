package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rocketpt.server.util.SecurityUtil;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@TableName("user_credential")
public class UserCredential extends EntityBase {

    /**
     * 标识（手机号 邮箱 用户名或第三方应用的唯一标识）
     */
    private String identifier;

    private Long userId;
    /**
     * 密码凭证（站内的保存密码，站外的不保存或保存token）
     */
    private String credential;

    private String passkey;

    /**
     * 两步认证
     */
    private String totp;
    /**
     * 登录类型（手机号 邮箱 用户名）或第三方应用名称（微信 微博等）
     */
    private IdentityType identityType;

    public boolean doCredentialMatch(String credential) {

        //TODO 未实现其他登录方式
        if (this.getIdentityType() != IdentityType.PASSWORD || !SecurityUtil.md5(identifier,
                credential).equals(this.getCredential())) {
            return false;
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
