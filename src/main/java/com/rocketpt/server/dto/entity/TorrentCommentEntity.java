package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 种子文件列表
 *
 * @author plexpt
 */
@Data
@NoArgsConstructor
@TableName("t_torrent_comment")
public class TorrentCommentEntity extends EntityBase {

    /**
     * ID
     */
    Integer id;

    /**
     * 关联的种子ID
     */
    Integer torrentId;

    /**
     * 用户ID
     */
    Integer userId;

    /**
     * 父级ID 楼中楼才有
     */
    Integer pid;

    /**
     * comment内容
     */
    String comment;


    /**
     * 添加日期
     */
    private LocalDateTime createTime;


}
