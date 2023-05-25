package com.rocketpt.server.dto.vo;


import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "TOTP")
public class TotpVo {

    @NotEmpty
    @Schema(description = "密钥")
    String key;

    @Schema(description = "二维码内容")
    String uri;


    @NotNull
    @Length(min = 6, max = 6)
    @Schema(description = "6位动态验证码")
    Integer code;
}
