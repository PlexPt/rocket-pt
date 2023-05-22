package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@Data
@TableName("t_organization")
public class OrganizationEntity extends EntityBase {

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String name;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Type type;

    //    @ManyToOne(fetch = FetchType.LAZY)
//    @TableField(exist = false)
//    private Organization parent;

    private String parentIds;
    private Long parentId;

    @TableField(exist = false)
    private Set<OrganizationEntity> children = new LinkedHashSet<>();

    public String makeSelfAsParentIds() {
        return getParentIds() + getId() + "/";
    }

    @RequiredArgsConstructor
    public enum Type {
        /**
         * 部门
         */
        DEPARTMENT(0),
        /**
         * 岗位
         */
        JOB(1);
        @EnumValue
        private final int code;
    }
}
