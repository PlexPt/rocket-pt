package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 * @author plexpt
 */
@Data
@TableName("t_suggest")
public class SuggestEntity extends EntityBase {


    @TableId
    private Integer id;
    /**
     * 角色名称
     */
    @Schema(description = "keyword")
    private String keyword;
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

}
