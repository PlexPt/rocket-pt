package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * 种子文件 torrent_file
 *
 * @author yzr
 * @deprecated 文件不存数据库
 */
@Data
@NoArgsConstructor
@TableName("t_torrent_file")
@Deprecated
public class TorrentFileEntity extends EntityBase {

    /**
     * 对应种子id
     */
    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer torrentId;

    /**
     * 二进制种子文件
     */
    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private byte[] file;

    /**
     * 种子协议版本
     */
    private IdentityType identityType;

    public TorrentFileEntity(Integer torrentId, byte[] file, IdentityType identityType) {
        this.torrentId = torrentId;
        this.file = file;
        this.identityType = identityType;
    }

    @RequiredArgsConstructor
    public enum IdentityType {
        V1(0),
        V2_ONLY(1),
        V2_COMPATIBILITY(2);

        @EnumValue
        //标记数据库存的值是code
        private final int code;

    }
}
