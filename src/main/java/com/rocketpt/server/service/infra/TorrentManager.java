package com.rocketpt.server.service.infra;

import com.rocketpt.server.dto.TorrentDto;
import com.rocketpt.server.dto.entity.TorrentFileEntity;

import java.security.NoSuchAlgorithmException;

/**
 * @author yzr
 */
public interface TorrentManager {

    TorrentDto parse(byte[] bytes);

    byte[] transform(byte[] bytes);

    byte[] infoHash(byte[] bytes) throws NoSuchAlgorithmException;

}
