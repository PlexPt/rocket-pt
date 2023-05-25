package com.rocketpt.server.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 种子存储服务
 *
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
public interface TorrentStorageService {

    default void init() {

    }

    void save(Integer id, byte[] torrent);

    void store(Integer id, MultipartFile file);

    InputStream load(Integer id);

    byte[] read(Integer id);

    void delete(Integer id);

}

