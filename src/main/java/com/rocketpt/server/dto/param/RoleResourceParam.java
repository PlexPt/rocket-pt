package com.rocketpt.server.dto.param;


import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "修改角色权限")
public class RoleResourceParam {

    @NotNull
    @Schema(description = "角色ID")
    Long roleId;

    @NotNull
    @Schema(description = "资源ID")
    List<Long> resourceIds;

}
