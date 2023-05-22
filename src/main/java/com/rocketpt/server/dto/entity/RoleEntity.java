package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

/**
 * 角色
 *
 * @author plexpt
 */
@Data
@TableName("t_role")
public class RoleEntity extends EntityBase {

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 备注
     */
    private String description;
    /**
     * 创建者ID
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private List<UserEntity> users = new ArrayList<>();
    @TableField(exist = false)
    private Set<MenuEntity> menuEntities = new LinkedHashSet<>();

}
