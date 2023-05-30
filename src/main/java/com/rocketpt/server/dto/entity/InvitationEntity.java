package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("t_invitation")
@Schema(description = "邀请")
public class InvitationEntity extends EntityBase {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 邀请人id
     */
    @Schema(description = "邀请人id")
    private Integer inviter;

    @Schema(description = "邀请邮箱")
    private String inviteeEmail;

    /**
     * 邀请码
     */
    @Schema(description = "邀请码")
    private String hash;

    /**
     * 是否有效
     * 1 有效 2 无效
     */
    @Schema(description = "是否有效")
    private Boolean valid;

    /**
     * 邀请时间
     */
    @Schema(description = "邀请时间")
    private LocalDateTime inviteTime;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "注册用户ID")
    private Integer userId;

    /**
     * 注册时间
     */
    @Schema(description = "注册时间")
    private LocalDateTime regTime;

}
