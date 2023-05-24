package com.rocketpt.server.service;

import com.rocketpt.server.util.TorrentUtils;

import org.springframework.stereotype.Service;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 本地种子存储
 */
@Slf4j
@Service
public class LocalTorrentStorageService implements TorrentStorageService {

    String defaultTorrentDir = TorrentUtils.getDefaultTorrentDir();

    @Override
    public void save(Integer id, byte[] torrent) {
        String path = defaultTorrentDir + id + ".torrent";

        FileUtil.writeBytes(torrent, path);

    }

    @Override
    public byte[] read(Integer id) {
        String path = defaultTorrentDir + id + ".torrent";

        return FileUtil.readBytes(path);
    }
}
