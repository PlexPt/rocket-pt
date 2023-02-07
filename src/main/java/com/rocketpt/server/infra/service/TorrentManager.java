package com.rocketpt.server.infra.service;

import com.rocketpt.server.dto.TorrentDto;
import com.rocketpt.server.dto.entity.TorrentFile;

import java.security.NoSuchAlgorithmException;

/**
 * @author yzr
 */
public interface TorrentManager {

    TorrentDto parse(byte[] bytes);

    byte[] transform(byte[] bytes);

    byte[] infoHash(byte[] bytes) throws NoSuchAlgorithmException;

    void preserve(Integer torrentId, byte[] bytes, TorrentFile.IdentityType identityType);
}
