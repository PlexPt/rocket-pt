package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 资源
 *
 * @author plexpt
 */
@Data
@TableName("resource")
public class MenuEntity extends EntityBase {
    @TableId
    private Long id;
    /**
     * 父菜单ID，一级菜单为0
     */
    private Long parentId;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单URL
     */
    private String url;
    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String permission;
    /**
     * 类型   10：目录   0：菜单   1：按钮
     */
    private Type type;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 排序
     */
    private Integer orderNum;

    @TableField(exist = false)
    private MenuEntity parent;

    @TableField(exist = false)
    private Set<MenuEntity> children = new LinkedHashSet<>();

    @RequiredArgsConstructor
    public enum Type {
        MENU(0),
        BUTTON(1);

        @EnumValue
        private final int code;
    }
}
