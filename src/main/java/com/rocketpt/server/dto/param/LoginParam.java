package com.rocketpt.server.dto.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 登录参数类
 */
@Data
public class LoginParam {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    /**
     * 验证码 UUID
     */
    private String uuid;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码
     */
    private Integer totp;

}
