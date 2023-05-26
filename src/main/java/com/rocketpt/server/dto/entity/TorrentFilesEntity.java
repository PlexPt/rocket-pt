package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 种子文件列表
 *
 * @author plexpt
 */
@Data
@NoArgsConstructor
@TableName("t_torrent_files")
public class TorrentFilesEntity extends EntityBase {

    /**
     * ID
     */
    Integer id;

    /**
     * 关联的种子ID
     */
    Integer torrentId;
    /**
     * 父级ID
     */
    Integer pid;

    /**
     * 文件的名称
     */
    String filename;

    /**
     * 文件的大小，单位为字节。
     */
    Long size;

}
