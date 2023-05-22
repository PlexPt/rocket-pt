package com.rocketpt.server.dto.param;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordParam {

    /**
     * 密码
     */
    @NotBlank(message = "email不能为空")
    private String email;

    /**
     * 验证码 UUID
     */
    @NotBlank(message = "不能为空")
    private String uuid;

    /**
     * 验证码
     */
    @NotBlank(message = "不能为空")
    private String code;

}
