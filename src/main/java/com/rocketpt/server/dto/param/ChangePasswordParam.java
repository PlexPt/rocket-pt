package com.rocketpt.server.dto.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(description = "修改密码")
public class ChangePasswordParam {

    @NotEmpty
    @Schema(description = "旧密码")
    private String oldPassword;
    @NotEmpty
    @Schema(description = "新密码")
    private String newPassword;

}
