package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 标签
 *
 * @author plexpt
 */
@Data
@TableName("t_tag")
public class TagEntity extends EntityBase {


    @TableId
    private Integer id;
    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "标签分类")
    private Integer cat;
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
