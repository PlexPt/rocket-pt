package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户凭证 敏感信息 user_credential
 *
 * @author plexpt
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_credential")
public class UserCredentialEntity extends EntityBase {
    /**
     * 用户ID
     */
    @TableId(type = IdType.INPUT)
    private Integer id;


    /**
     * 用户名
     */
    private String username;

    /**
     * password
     */
    private String password;

    /**
     * salt
     */
    private String salt;

    /**
     * passkey
     */
    private String passkey;
    /**
     * 邮箱校验码
     */
    private String checkCode;

    /**
     * 二步验证 totp
     */
    private String totp;


}
