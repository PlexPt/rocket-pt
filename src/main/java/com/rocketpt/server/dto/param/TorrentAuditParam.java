package com.rocketpt.server.dto.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-02-09 14:43:20
 */
@Data
public class TorrentAuditParam {

    @NotNull
    Integer id;


    /**
     * 审核状态  1 通过 2 不通过
     */
    @Schema(description = "状态 0 候选中 1 已发布 2 已下架")
    @NotNull
    Integer status;


    @Schema(description = "备注")
    String remark;


}
