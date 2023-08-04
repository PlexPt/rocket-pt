package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统配置信息表
 *
 * @author plexpt
 */
@Data
@TableName("t_sys_config")
public class SysConfigEntity {

    /**
     * key
     */
    @TableId(type = IdType.INPUT)
    @Schema(description = "配置KEY")
    private String k;
    /**
     * value
     */
    @Schema(description = "配置值")
    private String v;
    /**
     * 状态   0：隐藏   1：显示
     */
    @Schema(description = "状态 0：隐藏 1：显示")
    private Integer status;


    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

}
