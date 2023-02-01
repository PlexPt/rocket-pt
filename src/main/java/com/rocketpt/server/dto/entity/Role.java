package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Data;

/**
 * 角色
 *
 * @author plexpt
 */
@Data
@TableName("role")
public class Role extends EntityBase {

    //    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private Boolean available = Boolean.FALSE;

    //    @ManyToMany(fetch = FetchType.LAZY, cascade = DETACH)
//    @JoinTable(name = "role_resource",
//            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"))
    @TableField(exist = false)
    private Set<Resource> resources = new LinkedHashSet<>();

    //    @ManyToMany(fetch = FetchType.LAZY, cascade = DETACH)
//    @JoinTable(name = "user_role",
//            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @TableField(exist = false)
    private Set<User> users = new LinkedHashSet<>();

}
