package com.rocketpt.server.util;

import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.Type;
import com.rocketpt.server.dto.TorrentDto;
import com.rocketpt.server.dto.vo.TorrentFileVo;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author intent
 * @date 2019/7/24 12:50
 * @about <link href='http://zzyitj.xyz/'/>
 */
@Slf4j
public class TorrentUtils {
    private static final char[] HEX_SYMBOLS = "0123456789ABCDEF".toCharArray();

    /**
     * 只获取种子info的hash并返回
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static TorrentDto getTorrentHash(byte[] data) throws NoSuchAlgorithmException {
        Bencode bencode = new Bencode(true);
        Map<String, Object> torrentDataMap = bencode.decode(data, Type.DICTIONARY);
        Map<String, Object> infoMap = (Map<String, Object>) torrentDataMap.get("info");
        List<Object> files = (List<Object>) infoMap.get("files");
        long len = 0;
        long count = 0;
        if (files != null) {
            for (Object o : files) {
                Map<String, Object> pace = (Map<String, Object>) o;
                len += (long) pace.get("length");
                List<Object> path = (List<Object>) pace.get("path");
                for (Object value : path) {
                    ByteBuffer name = (ByteBuffer) value;
                    String s = new String(name.array());
                    if (s.contains(".")) {
                        count++;
                    }
                }
            }
        } else {
            len = (long) infoMap.get("length");
            count = 1;
        }
        TorrentDto torrent = new TorrentDto();
        torrent.setUkInfoHash(hash(bencode.encode(infoMap)));
        torrent.setTorrentSize(len);
        torrent.setTorrentCount(count);
        return torrent;
    }

    public static TorrentFileVo getTorrentFile(File file) throws NoSuchAlgorithmException {
        TorrentFileVo torrent = new TorrentFileVo();
        torrent.setFillName(file.getName());
        Bencode bencode = new Bencode(true);
        byte[] fileBytes = FileUtil.readBytes(file);
        Map<String, Object> torrentMap = bencode.decode(fileBytes, Type.DICTIONARY);
        ByteBuffer announceByteBuffer = (ByteBuffer) torrentMap.get("announce");
        torrent.setAnnounce(new String(announceByteBuffer.array()));
        Map<String, Object> infoMap = (Map<String, Object>) torrentMap.get("info");
        // info
        List<Object> files = (List<Object>) infoMap.get("files");
        // hash
        torrent.setHash(hash(bencode.encode(infoMap)));
        // length
        long len = 0;
        if (files != null) {
            for (Object o : files) {
                Map<String, Object> pace = (Map<String, Object>) o;
                len += (long) pace.get("length");
            }
        } else {
            len = (long) infoMap.get("length");
        }
        torrent.setLength(len);
        return torrent;
    }


    /**
     * hash
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] hash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest crypt;
        crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(data);
        return crypt.digest();
    }

    /**
     * 获取默认种子目录: jar所在目录/torrent
     *
     * @return
     */
    public static String getDefaultDir() {
        ApplicationHome home = new ApplicationHome();
        return home.getDir().getAbsolutePath();
    }

    /**
     * 获取默认种子目录: jar所在目录/torrent
     *
     * @return
     */
    public static String getDefaultTorrentDir() {
        return getDefaultDir() + "/torrent/";
    }

}
