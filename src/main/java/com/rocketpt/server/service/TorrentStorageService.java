package com.rocketpt.server.service;

import java.io.File;

/**
 * 种子存储服务
 *
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
public interface TorrentStorageService {

    void save(Long id, byte[] torrent);

    byte[] read(Long id);

}

