package com.rocketpt.server.dto.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 注册参数
 */
@Data
@Schema(description = "邀请")
public class InviteParam {

    private String username;

    private String email;

    @Schema(description = "邀请内容")
    private String content;

    @Schema(description = "备注")
    private String remark;

}
