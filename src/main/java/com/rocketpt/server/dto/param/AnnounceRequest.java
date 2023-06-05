package com.rocketpt.server.dto.param;

import com.rocketpt.server.dto.entity.TorrentEntity;
import com.rocketpt.server.dto.entity.UserEntity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Announce请求类
 */
@Data
@Slf4j
public class AnnounceRequest {

    /**
     * passkey
     */
    private String passkey;

    /**
     * 客户端ID
     */
    private byte[] peerId;

    /**
     * 事件
     */
    private String event;

    /**
     * IPv6地址
     */
    private String ipv6;

    /**
     * IPv4地址
     */
    private String ipv4;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 密钥
     */
    private String key;

    /**
     * hash
     */
    private byte[] infoHash;

    /**
     * 客户端ID（16进制）
     */
    private String peerIdHex;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 需要的同伴数
     */
    private Integer numWant;

    /**
     * 已下载量
     */
    private Long downloaded = -1L;

    /**
     * 已上传量
     */
    private Long uploaded = -1L;

    /**
     * 剩余量
     */
    private Long left = -1L;

    /**
     * 紧凑型标识
     */
    private Integer compact;

    /**
     * 没有 ID标识
     */
    private Integer noPeerId;

    /**
     * 做种
     */
    private Boolean seeder;

    /**
     * 远程地址
     */
    private String remoteAddr;

    /**
     * 客户端信息
     */
    private String userAgent;

    /**
     * 想要的摘要信息
     */
    private String wantDigest;

    /**
     * 用户实体
     */
    private UserEntity user;

    /**
     * 用户实体
     */
    private TorrentEntity torrent;

    /**
     * 获取hash
     */
    public String getHash() {
        return passkey;
    }
}
