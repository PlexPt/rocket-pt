package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色与菜单对应关系
 *
 * @author plexpt
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_role_resource")
public class RoleResourceEntity {

    /**
     *
     */
    @TableId
    private Integer id;
    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 菜单ID
     */
    private Integer resourceId;

}
