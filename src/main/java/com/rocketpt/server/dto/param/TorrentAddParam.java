package com.rocketpt.server.dto.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-02-09 14:43:20
 */
@Data
public class TorrentAddParam {


    /**
     * 名称
     */
    @NotEmpty
    @Schema(description = "简称")
    private String name;

    /**
     * 标题
     */
    @NotEmpty
    @Schema(description = "标题")
    private String title;
    /**
     * 简介副标题
     */
    @NotEmpty
    @Schema(description = "副标题")
    private String subheading;
    /**
     * 封面
     */
    @Schema(description = "封面")
    private String cover;
    /**
     * 描述
     */
    @NotEmpty
    @Schema(description = "描述")
    private String description;

    /**
     * 类别
     */
    @NotNull
    @Schema(description = "类别")
    private Integer category;

    /**
     * 状态 0 候选中 1 已发布 2 已下架
     */
    @Schema(description = "状态 0 候选中 1 已发布 2 已下架")
    private Integer status;

    /**
     * 是否匿名 0 否 1 是
     */
    @Schema(description = "是否匿名 0 否 1 是")
    private Integer anonymous;


    @Schema(description = "备注")
    private String remark;


}
