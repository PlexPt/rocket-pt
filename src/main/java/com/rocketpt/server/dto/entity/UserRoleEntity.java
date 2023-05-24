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
@TableName("t_user_role")
public class UserRoleEntity {

    /**
     *
     */
    @TableId
    private Integer id;
    private Integer userId;
    private Integer roleId;

}
