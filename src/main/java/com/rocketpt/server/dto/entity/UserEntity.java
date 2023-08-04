package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rocketpt.server.common.exception.RocketPTException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-02-09 12:01:16
 */
@Data
@TableName("t_user")
public class UserEntity {


    @TableId
    private Integer id;
    /**
     * 账户名
     */
    @Schema(description = "username")
    private String username;
    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;
    /**
     * 注册时间
     */
    @Schema(description = "注册时间")
    private LocalDateTime createTime;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;
    /**
     * 性别 0男 1女 2其他
     */
    @Schema(description = "性别 0男 1女 2其他")
    private Integer gender;
    /**
     * 状态 0正常 1 已锁定 2未激活
     */
    @Schema(description = "状态 0正常 1 已锁定 2未激活")
    private Integer state;
    /**
     * 邮件地址
     */
    @Schema(description = "email")
    private String email;

    /**
     * 管理备注
     */
    @Schema(description = "管理备注")
    private String remark;
    /**
     * 上次登录时间
     */
    @Schema(description = "上次登录时间")
    private LocalDateTime lastLogin;
    /**
     * 上次访问时间
     */
    @Schema(description = "上次访问时间")
    private LocalDateTime lastAccess;

    /**
     * 上次发布offer时间
     */
    private LocalDateTime lastOffer;

    /**
     * 隐私级别 0 1 2
     */
    @Schema(description = "隐私级别")
    private Integer privacy;
    /**
     * 用户等级
     */
    private Integer level;
    /**
     * 用户最大等级
     */
    private Integer levelMax;
    /**
     * 上传量
     */
    @Schema(description = "上传量")
    private Long uploaded;
    /**
     * 下载量
     */
    @Schema(description = "下载量")
    private Long downloaded;
    /**
     * 做种时间
     */
    @Schema(description = "做种时间")
    private Long seedtime;
    /**
     * 下载时间
     */
    @Schema(description = "下载时间")
    private Long leechtime;

    /**
     * 警告
     */
    @Schema(description = "警告状态")
    private Boolean warning;
    /**
     * 警告者
     */
    @Schema(description = "警告者")
    private Long warningBy;
    /**
     * 总警告次数
     */
    @Schema(description = "总警告次数")
    private Integer warningTimes;
    /**
     * 警告到期时间
     */
    @Schema(description = "警告到期时间")
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
    @Schema(description = "上家ID")
    private Integer inviter;
    /**
     * 魔力积分
     */
    @Schema(description = "魔力积分")
    private Long bonus;
    /**
     * 经验值
     */
    @Schema(description = "经验值")
    private Long exp;

    @RequiredArgsConstructor
    public enum Gender {
        MALE(0),
        FEMALE(1),
        OTHER(2);

        @EnumValue
        @Getter
        private final int code;

        public static Gender valueof(Integer value) {
            if (value == null) {
                return OTHER;
            }

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
        @Getter
        private final int code;
    }

    public boolean userLocked() {
        return this.state == State.LOCKED.code;
    }

    /**
     * @return 用户状态正常
     */
    public boolean isUserOK() {

        return this.state == State.NORMAL.code;
    }


}
