package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
@Data
@TableName("torrents")
public class TorrentsEntity {

    /**
     * id
     */
    @TableId
    private Integer id;
    /**
     * 种子hash
     */
    private byte[] infoHash;
    /**
     * 标题
     */
    private String name;
    /**
     * 上传文件名
     */
    private String filename;
    /**
     * 下载文件名
     */
    private String saveAs;
    /**
     *
     */
    private String cover;
    /**
     * 简介
     */
    private String descr;
    /**
     * 副标题
     */
    private String smallDescr;
    /**
     *
     */
    private String oriDescr;
    /**
     * 分类
     */
    private Integer category;
    /**
     *
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
     * 体积
     */
    private Long size;
    /**
     *
     */
    private Date added;
    /**
     * Enum
     */
    private String type;
    /**
     *
     */
    private Integer numfiles;
    /**
     *
     */
    private Integer comments;
    /**
     *
     */
    private Integer views;
    /**
     *
     */
    private Integer hits;
    /**
     *
     */
    private Integer timesCompleted;
    /**
     *
     */
    private Integer leechers;
    /**
     *
     */
    private Integer seeders;
    /**
     *
     */
    private Date lastAction;
    /**
     * Enum
     */
    private String visible;
    /**
     * Enum
     */
    private String banned;
    /**
     *
     */
    private Integer owner;

    /**
     *
     */
    private Integer spState;
    /**
     *
     */
    private Integer promotionTimeType;
    /**
     *
     */
    private Date promotionUntil;
    /**
     * Enum
     */
    private String anonymous;
    /**
     *
     */
    private Integer url;
    /**
     *
     */
    private String posState;
    /**
     *
     */
    private Date posStateUntil;
    /**
     *
     */
    private Integer cacheStamp;
    /**
     * Enum
     */
    private String picktype;
    /**
     *
     */
    private Date picktime;
    /**
     *
     */
    private Date lastReseed;
    /**
     *
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
     *
     */
    private Integer approvalStatus;

}
