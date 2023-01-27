package com.rocketpt.server.web.entity;

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
     *
     */
    private byte[] infoHash;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private String filename;
    /**
     *
     */
    private String saveAs;
    /**
     *
     */
    private String cover;
    /**
     *
     */
    private String descr;
    /**
     *
     */
    private String smallDescr;
    /**
     *
     */
    private String oriDescr;
    /**
     *
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
     *
     */
    private Long size;
    /**
     *
     */
    private Date added;
    /**
     *
     */
    private Enum type;
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
     *
     */
    private Enum visible;
    /**
     *
     */
    private Enum banned;
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
     *
     */
    private Enum anonymous;
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
     *
     */
    private Enum picktype;
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
