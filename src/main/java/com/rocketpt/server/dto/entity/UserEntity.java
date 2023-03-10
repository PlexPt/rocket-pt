package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-02-09 12:01:16
 */
@Data
@TableName("user")
public class UserEntity {


    @TableId
    private Long id;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 全名
     */
    private String fullName;
    /**
     * 性别 0男 1女 2其他
     */
    private Integer gender;
    /**
     * 状态 0正常 1 已锁定 2未激活
     */
    private Integer state;
    /**
     * 账户名
     */
    private String username;
    /**
     * 组织ID
     */
    private Long organizationId;
    /**
     * 邮件地址
     */
    private String email;
    /**
     * 注册时间
     */
    private Date added;
    /**
     * 上次登录时间
     */
    private Date lastLogin;
    /**
     * 上次访问时间
     */
    private Date lastAccess;
    /**
     * 上次访问主页时间
     */
    private Date lastHome;
    /**
     * 上次发布offer时间
     */
    private Date lastOffer;
    /**
     * 上次访问论坛时间
     */
    private Date forumAccess;
    /**
     * 上次接收工作人员消息时间
     */
    private Date lastStaffmsg;
    /**
     * 上次接收私人消息时间
     */
    private Date lastPm;
    /**
     * 上次发表评论时间
     */
    private Date lastComment;
    /**
     * 上次发表帖子时间
     */
    private Date lastPost;
    /**
     * 上次浏览
     */
    private Date lastActive;
    /**
     * 隐私级别 0 1 2
     */
    private Integer privacy;
    /**
     * 注册IP
     */
    private String regIp;
    /**
     * 用户等级
     */
    private Integer level;
    /**
     * 上传量
     */
    private Long uploaded;
    /**
     * 下载量
     */
    private Long downloaded;
    /**
     * 做种时间
     */
    private Long seedtime;
    /**
     * 下载时间
     */
    private Long leechtime;
    /**
     * 管理备注
     */
    private String modcomment;
    /**
     * 警告者
     */
    private Long warningBy;
    /**
     * 总警告次数
     */
    private Integer warningTimes;
    /**
     * 警告
     */
    private Boolean warning;
    /**
     * 警告到期时间
     */
    private Date warningUntil;
    /**
     *
     */
    private Integer download;
    /**
     *
     */
    private Integer upload;
    /**
     * 上家
     */
    private Integer invitedBy;
    /**
     * 魔力积分
     */
    private Long bonus;
    /**
     * 经验值
     */
    private Long exp;

}
