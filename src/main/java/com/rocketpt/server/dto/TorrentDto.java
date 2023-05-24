package com.rocketpt.server.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import lombok.Data;

/**
 * @author intent
 * @date 2019/8/1 16:18
 * @about <link href='http://zzyitj.xyz/'/>
 */
@Data
@TableName(value = "t_torrent")
public class TorrentDto {

    @TableId(type = IdType.AUTO)
    private Integer id;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime created;
    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modified;
    private Long fkUserAuthId;
    private Long fkTorrentDiscountId;
    private byte[] ukInfoHash;
    private String fileName;
    private Long fileSize;
    private String filePath;
    private Long torrentSize;
    private Long torrentCount;
    private Boolean isDelete;
    private Map<String, Object> dict;
}
