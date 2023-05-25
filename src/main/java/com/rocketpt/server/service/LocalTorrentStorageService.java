package com.rocketpt.server.service;

import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.util.TorrentUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import cn.hutool.core.io.FileUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


/**
 * 本地种子存储
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LocalTorrentStorageService implements TorrentStorageService {


    final SysConfigService sysConfigService;


    /**
     * 使用绝对路径
     */
    @Getter
    @Setter
    boolean useAbsolutePath = false;

    @Getter
    @Setter
    String absolutePath = "/torrent/";

    @Getter
    @Setter
    String relativePath = "/torrent/";


    String defaultDir = TorrentUtils.getDefaultDir();


    @PostConstruct
    @Override
    @SneakyThrows
    public void init() {
        useAbsolutePath = sysConfigService.getUseAbsolutePath();
        relativePath = sysConfigService.getTorrentStoragePath();
        absolutePath = relativePath;

        FileUtil.mkdir(getPath());
    }


    @Override
    public void save(Integer id, byte[] torrent) {
        String path = getFilePath(id);

        FileUtil.writeBytes(torrent, path);
    }

    public String getPath() {
        if (useAbsolutePath) {
            return absolutePath;
        }

        return defaultDir + relativePath;
    }

    public String getFilePath(int id) {
        String path = getPath() + id + ".torrent";

        return path;
    }

    @Override
    @SneakyThrows
    public void store(Integer id, MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // validation here for .torrent file
        if (!filename.endsWith(".torrent")) {
            throw new RocketPTException("Invalid file type. Only .torrent files are allowed");
        }

        if (file.isEmpty()) {
            throw new RocketPTException("Failed to store empty file " + filename);
        }
        if (filename.contains("..")) {
            // This is a security check
            throw new RocketPTException("Cannot store file with relative path outside current" +
                    " directory "
                    + filename);
        }

        try (InputStream inputStream = file.getInputStream()) {
            FileUtil.writeFromStream(inputStream, getFilePath(id));
        }

    }

    @Override
    public byte[] read(Integer id) {
        String path = getFilePath(id);

        return FileUtil.readBytes(path);
    }

    @Override
    public void delete(Integer id) {
        String path = getFilePath(id);
        FileUtil.del(path);
    }

    @Override
    public InputStream load(Integer id) {
        String path = getFilePath(id);
        return FileUtil.getInputStream(path);

    }

}
