package com.rocketpt.server.sys.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class Resource extends EntityBase {

    private String name;

    private Type type;

    private String permission;

    //    @ManyToOne(fetch = FetchType.LAZY)
    @TableField(exist = false)
    private Resource parent;

    //    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @TableField(exist = false)
    private Set<Resource> children = new LinkedHashSet<>();

    private String parentIds; //父编号列表
    /**
     * 父编号
     */
    private Long parentId;

    private String icon;
    private String url;

    @RequiredArgsConstructor
    public enum Type {
        MENU(0),
        BUTTON(1);

        @EnumValue
        private final int code;
    }
}
