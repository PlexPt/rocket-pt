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
public class TorrentPeerEntity extends EntityBase {

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
     * Peer的客户端ID，表示在P2P网络中的唯一标识，如 "-qB4520-Lw~GW1WMV5ZO"
     */
    String peerId;

    /**
     * 用户IP地址
     */
    String ip;

    /**
     * 用户IPV6地址
     */
    String ipv6;

    /**
     * 用户的网络端口号
     */
    Integer port;

    /**
     * 用户已经上传的数据量（单位：字节）
     */
    Long uploaded;

    /**
     * 用户已经下载的数据量（单位：字节）
     */
    Long downloaded;

    /**
     * 用户剩余需要下载的数据量（单位：字节）
     */
    Long left;

    /**
     * 本次会话的下载量偏移（单位：字节）
     */
    Long downloadoffset;

    /**
     * 本次会话的上传量偏移（单位：字节）
     */
    Long uploadoffset;

    /**
     * 是否是做种者，如果是，则值为true
     */
    Boolean seeder;

    /**
     * 是否可连接，如果是，则值为true
     */
    Boolean connectable;

    /**
     * 是否使用种子盒，如果是，则值为true
     */
    Boolean seedBox;

    /**
     * 用户使用的客户端的用户代理，例如 "qBittorrent/4.5.2"
     */
    String userAgent;

    /**
     * 用户的秘钥，用于身份验证
     */
    String passkey;

    /**
     * 用户添加种子的时间
     */
    LocalDateTime createTime;

    /**
     * 用户开始下载或者上传种子的时间
     */
    LocalDateTime startTime;

    /**
     * 用户完成下载或者上传种子的时间
     */
    LocalDateTime finishTime;

    /**
     * 用户最近一次连接到tracker的时间
     */
    LocalDateTime lastAnnounce;

    /**
     * 用户上一次连接到tracker的时间
     */
    LocalDateTime preAnnounce;


}
