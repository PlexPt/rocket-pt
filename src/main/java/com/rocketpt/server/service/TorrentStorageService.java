package com.rocketpt.server.service;

/**
 * 种子存储服务
 *
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
public interface TorrentStorageService {

    void save(Integer id, byte[] torrent);

    byte[] read(Integer id);

}

