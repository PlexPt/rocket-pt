package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-02-09 14:43:20
 */
@Data
@TableName("t_torrents")
public class TorrentsEntity {

    /**
     *
     */
    @TableId
    private Integer id;
    /**
     * 种子哈希
     */
    private byte[] infoHash;
    /**
     * 名称
     */
    private String name;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 存储名称
     */
    private String saveAs;
    /**
     * 封面
     */
    private String cover;
    /**
     * 描述
     */
    private String descr;
    /**
     * 简介副标题
     */
    private String smallDescr;
    /**
     * 原始描述
     */
    private String oriDescr;
    /**
     * 类别
     */
    private Integer category;
    /**
     * 来源
     */
    private Integer source;
    /**
     *
     */
    private Integer medium;
    /**
     *
     */
    private Integer codec;
    /**
     *
     */
    private Integer standard;
    /**
     *
     */
    private Integer processing;
    /**
     *
     */
    private Integer team;
    /**
     *
     */
    private Integer audiocodec;
    /**
     * 大小
     */
    private Long size;
    /**
     * 添加日期
     */
    private LocalDateTime added;
    /**
     * 类型
     */
    private Type type;
    /**
     * 文件数量
     */
    private Integer numfiles;
    /**
     * 评论数
     */
    private Integer comments;
    /**
     * 浏览次数
     */
    private Integer views;
    /**
     * 点击次数
     */
    private Integer hits;
    /**
     * 完成次数
     */
    private Integer timesCompleted;
    /**
     * 下载数
     */
    private Integer leechers;
    /**
     * 做种数
     */
    private Integer seeders;
    /**
     * 最后操作日期
     */
    private LocalDateTime lastAction;
    /**
     * 可见性
     */
    private String visible;
    /**
     *
     */
    private String banned;
    /**
     * 拥有者
     */
    private Integer owner;
    /**
     *
     */
    private Integer spState;
    /**
     * 促销时间类型
     */
    private Integer promotionTimeType;
    /**
     * 促销截止日期
     */
    private LocalDateTime promotionUntil;
    /**
     * 匿名
     */
    private String anonymous;
    /**
     *
     */
    private Integer url;
    /**
     * 位置状态
     */
    private String posState;
    /**
     * 位置状态截止日期
     */
    private LocalDateTime posStateUntil;
    /**
     *
     */
    private Integer cacheStamp;
    /**
     * 推荐类型
     */
    private String picktype;
    /**
     * 推荐日期
     */
    private LocalDateTime picktime;
    /**
     * 最后做种日期
     */
    private LocalDateTime lastReseed;
    /**
     * ptgen生成内容
     */
    private String ptGen;
    /**
     *
     */
    private String technicalInfo;
    /**
     *
     */
    private Integer hr;
    /**
     * 审批状态
     */
    private Integer approvalStatus;

    @RequiredArgsConstructor
    public enum Type {
        single(1),
        multi(2);

        @EnumValue
        private final int code;
    }

}
