package com.rocketpt.server.dto.vo;

import java.time.LocalDateTime;

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
public class TorrentVO {

    private Integer id;

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


    /**
     * 文件状态 0 未上传 1 已上传
     */
    private Integer fileStatus;
    /**
     * 审核人
     */
    @Schema(description = "审核人")
    private Integer reviewer;


    /**
     * 添加日期
     */
    @Schema(description = "添加日期")
    private LocalDateTime createTime;


    /**
     * 修改日期
     */
    @Schema(description = "修改日期")
    private LocalDateTime updateTime;

    /**
     * 拥有者
     */
    @Schema(description = "拥有者")
    private Integer owner;
    /**
     * 文件大小
     */
    @Schema(description = "文件大小")
    private Long size;
    /**
     * 类型
     * single(1)
     * multi(2)
     */
    private Integer type;
    /**
     * 文件数量
     */
    @Schema(description = "文件数量")
    private Integer fileCount;

    /**
     * 评论数
     */
    @Schema(description = "评论数")
    private Integer comments;
    /**
     * 浏览次数
     */
    @Schema(description = "浏览次数")
    private Integer views;
    /**
     * 点击次数
     */
    @Schema(description = "点击次数")
    private Integer hits;

    /**
     * 可见性
     */
    @Schema(description = "可见性")
    private Integer visible;

    /**
     * 下载数
     */
    @Schema(description = "下载数")
    private Integer leechers;
    /**
     * 做种数
     */
    @Schema(description = "做种数")
    private Integer seeders;

    /**
     * 完成次数
     */
    @Schema(description = "完成次数")
    private Integer completions;

}
