package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 标签分类
 *
 * @author plexpt
 */
@Data
@TableName("t_tag_cat")
public class TagCatEntity extends EntityBase {


    @TableId
    private Integer id;
    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "分类类型 可以是 多选、单选 或 必选一个")
    private Integer type;


}
