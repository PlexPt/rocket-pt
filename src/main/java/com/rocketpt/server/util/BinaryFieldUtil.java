package com.rocketpt.server.util;

import com.rocketpt.server.common.exception.TrackerException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;

import java.util.List;
import java.util.regex.Pattern;

public class BinaryFieldUtil {
    interface RegexPattern {
        Pattern INFO_HASH = Pattern.compile("(?)info_hash=([^&]*)");
        Pattern PEER_ID = Pattern.compile("(?)peer_id=([^&]*)");
    }

    static private final URLCodec CODEC = new URLCodec();

    static public List<byte[]> matchInfoHashesHex(String queryStrings) {
        return RegexPattern.INFO_HASH.matcher(queryStrings)
                .results()
                .map(x -> x.group(1))
                .map(BinaryFieldUtil::decodeUrl)
                .toList();
    }

    static public byte[] matchInfoHash(String queryStrings) {
        return RegexPattern.INFO_HASH.matcher(queryStrings)
                .results()
                .map(x -> x.group(1))
                .map(BinaryFieldUtil::decodeUrl)
                .findFirst().orElseThrow(() -> new TrackerException("missing field info_hash"));
    }

    static public byte[] matchPeerId(String queryStrings) {
        return RegexPattern.PEER_ID.matcher(queryStrings)
                .results()
                .map(x -> x.group(1))
                .map(BinaryFieldUtil::decodeUrl)
                .findFirst().orElseThrow(() -> new TrackerException("missing field peer_id"));
    }

    static private byte[] decodeUrl(String url) {
        try {
            return CODEC.decode(url.getBytes());
        } catch (DecoderException e) {
            throw new TrackerException("unable to decode input data " + url, e);
        }
    }
}
