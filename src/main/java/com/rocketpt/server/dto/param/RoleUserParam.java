package com.rocketpt.server.dto.param;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema(description = "调整角色用户")
public class RoleUserParam {

    @NotNull
    @Schema(description = "角色ID")
    Integer roleId;

    @NotNull
    @Schema(description = "用户ID")
    List<Integer> userIds;

}
