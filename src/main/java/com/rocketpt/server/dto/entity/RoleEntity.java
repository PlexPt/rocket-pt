package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private Integer id;
    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String name;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
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


    @Schema(description = "角色菜单ID")
    @TableField(exist = false)
    private List<Integer> resourceIds = new ArrayList<>();

    @TableField(exist = false)
    private List<UserEntity> users = new ArrayList<>();

    @TableField(exist = false)
    private Set<ResourceEntity> resourceEntities = new LinkedHashSet<>();

}
