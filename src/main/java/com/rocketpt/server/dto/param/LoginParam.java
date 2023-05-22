package com.rocketpt.server.dto.param;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "用户名")
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
    @Schema(description = "验证码 UUID")
    private String uuid;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String code;

    /**
     * 验证码
     */
    @Schema(description = "两步验证码")
    private Integer totp;

}
