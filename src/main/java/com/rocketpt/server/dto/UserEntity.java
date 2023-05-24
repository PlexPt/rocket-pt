package com.rocketpt.server.dto;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.entity.EntityBase;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.RequiredArgsConstructor;


/**
 * 用户
 *
 * @author plexpt
 */
@Data
@TableName("user")
public class UserEntity extends EntityBase {

    /**
     * 头像
     */
    private String avatar;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 全名
     */
    private String fullName;
    /**
     * 性别 0男 1女 2其他
     */
    private Gender gender;
    /**
     * 状态 0正常 1 已锁定 2未激活
     */
    private State state;

    /**
     * 账户名
     */
    private String username;

    /**
     * 邮件地址
     */
    private String email;
    /**
     * 注册时间
     */
    private LocalDateTime added;
    /**
     * 上次登录时间
     */
    private LocalDateTime lastLogin;
    /**
     * 上次访问时间
     */
    private LocalDateTime lastAccess;
    /**
     * 上次访问主页时间
     */
    private LocalDateTime lastHome;
    /**
     * 上次发布offer时间
     */
    private LocalDateTime lastOffer;
    /**
     * 上次访问论坛时间
     */
    private LocalDateTime forumAccess;
    /**
     * 上次接收工作人员消息时间
     */
    private LocalDateTime lastStaffmsg;
    /**
     * 上次接收私人消息时间
     */
    private LocalDateTime lastPm;
    /**
     * 上次发表评论时间
     */
    private LocalDateTime lastComment;
    /**
     * 上次发表帖子时间
     */
    private LocalDateTime lastPost;
    /**
     * 上次浏览
     */
    private LocalDateTime lastActive;
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
    private LocalDateTime warningUntil;
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

    private String checkCode;

    /**
     * 注册类型
     * <p>
     * 0.系统手动添加
     * 1.开放注册
     * 2.受邀注册
     * 3.自助答题注册
     */
    private Integer regType;


    @RequiredArgsConstructor
    public enum Gender {
        MALE(0),
        FEMALE(1),
        OTHER(2);

        @EnumValue
        private final int code;

        public static Gender valueof(int value) {
            for (Gender gender : Gender.values()) {
                if (gender.code == value) {
                    return gender;
                }
            }

            throw new RocketPTException("性别错误");
        }
    }

    @RequiredArgsConstructor
    public enum State {
        NORMAL(0),
        LOCKED(1),
        INACTIVATED(2);
        @EnumValue
        private final int code;
    }

    public boolean userLocked() {
        return this.state == State.LOCKED;
    }


}
