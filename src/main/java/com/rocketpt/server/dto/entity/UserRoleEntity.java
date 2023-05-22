package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色
 *
 * @author plexpt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_role")
public class UserRoleEntity {

    /**
     *
     */
    @TableId
    private Long id;
    private Long userId;
    private Long roleId;

}
