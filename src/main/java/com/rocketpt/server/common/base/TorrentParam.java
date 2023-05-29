package com.rocketpt.server.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 种子查询
 */
@Data
@Schema(description = "种子查询")
public class TorrentParam extends OrderPageParam {


    @Schema(description = "关键字")
    String keyword;


    @Schema(description = "分类")
    Integer category;

    @Schema(description = "状态")
    Integer status;

}
