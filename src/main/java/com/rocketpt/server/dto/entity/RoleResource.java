package com.rocketpt.server.dto.entity;

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
@TableName("role_resource")
public class RoleResource {

    private Long roleId;
    private Long resourceId;

}
