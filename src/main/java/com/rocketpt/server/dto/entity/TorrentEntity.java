package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-02-09 14:43:20
 */
@Data
@TableName("t_torrent")
public class TorrentEntity {

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
     * 上传文件名
     */
    private String filename;
    /**
     * 标题
     */
    private String title;
    /**
     * 简介副标题
     */
    private String subheading;
    /**
     * 封面
     */
    private String cover;
    /**
     * 描述
     */
    private String description;

    /**
     * 类别
     */
    private Integer category;

    /**
     * 状态
     *
     * @see Status
     */
    private Integer status;

    /**
     * 文件状态 0 未上传 1 已上传
     */
    private Integer fileStatus;
    /**
     * 审核人
     */
    private Integer reviewer;


    /**
     * 添加日期
     */
    private LocalDateTime createTime;


    /**
     * 修改日期
     */
    private LocalDateTime updateTime;

    /**
     * 拥有者
     */
    private Integer owner;
    /**
     * 文件大小
     */
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
    private Integer fileCount;

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
     * 可见性
     */
    private Integer visible;

    /**
     * 是否匿名
     */
    private Integer anonymous;


    /**
     * 下载数
     */
    private Integer leechers;
    /**
     * 做种数
     */
    private Integer seeders;

    /**
     * 完成次数
     */
    private Integer completions;

    /**
     *
     */
    private String remark;

    /**
     * 种子状态
     * 0 候选中 1 已发布 2 审核不通过 3 已上架修改重审中 10 已下架
     */
    public interface Status {

        int CANDIDATE = 0;

        int PUBLISHED = 1;

        int AUDIT_NOT_PASSED = 2;

        int RETRIAL = 3;

        int REMOVED = 10;

    }

    @RequiredArgsConstructor
    public enum Type {
        single(1),
        multi(2);

        @Getter
        @EnumValue
        private final int code;
    }

    public boolean isStatusOK() {


        return status == 1;
    }

}
