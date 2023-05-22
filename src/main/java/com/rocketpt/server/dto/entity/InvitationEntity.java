package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("t_invitation")
public class InvitationEntity extends EntityBase {

    /**
     * 邀请人id
     */
    private Integer inviter;

    private String inviteeEmail;

    /**
     * 邀请码
     */
    private String hash;

    /**
     * 是否有效
     * 1 有效 2 无效
     */
    private Boolean valid;

    /**
     * 邀请时间
     */
    private LocalDateTime inviteTime;
    private String username;
    private Long userId;

    /**
     * 注册时间
     */
    private LocalDateTime regTime;
    private String taskid;
    private String orderid;
}
