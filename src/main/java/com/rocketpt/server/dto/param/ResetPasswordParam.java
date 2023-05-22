package com.rocketpt.server.dto.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "重置密码")
public class ResetPasswordParam {

    /**
     * 密码
     */
    @NotBlank(message = "email不能为空")
    private String checkCode;

    @NotBlank(message = "不能为空")
    @Schema(description = "新密码")
    private String newPassword;

    /**
     * 验证码 UUID
     */
    @NotBlank(message = "不能为空")
    @Schema(description = "验证码 UUID")
    private String uuid;

    /**
     * 验证码
     */
    @NotBlank(message = "不能为空")
    @Schema(description = "验证码")
    private String code;

}
