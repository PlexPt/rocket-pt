package com.rocketpt.server.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema(description = "站点配置")
public class SiteConfig {

    /**
     * 站点名称
     */
    @NotNull
    @Schema(description = "站点名称")
    private String name;

    /**
     * 网站标题
     */
    @Schema(description = "网站标题")
    private String title;


    /**
     * 网站Logo URL
     */
    @Schema(description = "网站Logo URL")
    private String logo;
    /**
     * 网站favicon URL
     */
    @Schema(description = "网站favicon")
    private String favicon;

    @Schema(description = "网站背景图片")
    private String bg;

    /**
     * 网站描述
     */
    @Schema(description = "网站描述")
    private String description;

    /**
     * 网站关键词
     */
    @Schema(description = "网站关键词")
    private String keywords;


    /**
     * 网站版权声明
     */
    @Schema(description = "网站版权声明")
    private String copyright;


    /**
     * 网站底部导航（以JSON格式存储）
     */
    @Schema(description = "网站底部导航（以JSON格式存储）")
    private String footerNavigation;

    /**
     * 管理
     */
    private String masterId;

    /**
     * 教程链接
     */
    private String tutorialLink;

    /**
     * 网站主题颜色
     */
    private String themeColor;

    /**
     * 社交媒体链接（以JSON格式存储）
     */
    private String socialLinks;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * remark
     */
    @Schema(description = "备注")
    private String remark;
    /**
     * 联系地址
     */
    private String contactAddress;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}

