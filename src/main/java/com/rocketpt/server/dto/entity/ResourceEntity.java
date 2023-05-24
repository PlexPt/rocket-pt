package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 资源
 *
 * @author plexpt
 */
@Data
@TableName("t_resource")
public class ResourceEntity extends EntityBase {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 父菜单ID，一级菜单为0
     */
    @Schema(description = "父ID")
    private Integer pid;
    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    private String name;
    /**
     * 菜单URL
     */
    @Schema(description = "路由地址")
    private String url;
    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @Schema(description = "权限字符串")
    private String permission;
    /**
     * 类型   0：目录   1：菜单   2：按钮  3：外链
     */
    @Schema(description = "类型 0：目录   1：菜单   2：按钮  3：外链")
    private Type type;
    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    private String icon;
    /**
     * 排序
     */
    @Schema(description = "显示顺序")
    private Integer sort;

    /**
     * 菜单状态（1显示 0隐藏）
     */
    @Schema(description = "菜单状态 1显示 0隐藏")
    private Integer status;

    /**
     * 创建者ID
     */
    @Schema(description = "创建者ID")
    private Integer createBy;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新者ID
     */
    @Schema(description = "更新者ID")
    private Integer updateBy;
    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 上级菜单名称
     */
    @TableField(exist = false)
    private String parentName;

    @TableField(exist = false)
    private ResourceEntity parent;

    @TableField(exist = false)
    private List<ResourceEntity> children = new ArrayList<>();

    @RequiredArgsConstructor
    public enum Type {
        DIR(0),
        MENU(1),
        BUTTON(2),
        LINK(3);

        @Getter
        @EnumValue
        private final int code;
    }
}
