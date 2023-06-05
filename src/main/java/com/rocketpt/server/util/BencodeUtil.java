package com.rocketpt.server.util;

import com.dampcake.bencode.BencodeOutputStream;
import com.rocketpt.server.common.exception.TrackerException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class BencodeUtil {

    static final String exceptionMsg = "Oops! Your request is like an alien language to us, we " +
            "can't encode it!";
    public interface Errors {
        String CLIENT_ERROR = BencodeUtil.error("Oops! Your request is like an alien language to " +
                "us, we can't decode it!");
        String INTERNAL_ERROR_60 = BencodeUtil.error("Give us a moment, try again later!", 60);
        String INTERNAL_ERROR_120 = BencodeUtil.error("Hold your horses! then try again later!",
                120);


    }

    public static String error(String reason) {
        return encode(Map.of("failure reason", reason));
    }

    public static String error() {
        return Errors.CLIENT_ERROR;
    }


    static public String error(String reason, Integer retry) {
        return encode(Map.of("failure reason", reason, "retry in", retry));
    }

    static public <K, V> String encode(Map<K, V> data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (BencodeOutputStream bencoder = new BencodeOutputStream(out)) {
                bencoder.writeDictionary(data);
                return out.toString();
            }
        } catch (IOException e) {
            throw new TrackerException(exceptionMsg, e);
        }
    }

    static public String encode(Integer data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (BencodeOutputStream bencoder = new BencodeOutputStream(out)) {
                bencoder.write(data);
                return out.toString();
            }
        } catch (IOException e) {
            throw new TrackerException(exceptionMsg, e);
        }
    }

    static public String encode(Number data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (BencodeOutputStream bencoder = new BencodeOutputStream(out)) {
                bencoder.writeNumber(data);
                return out.toString();
            }
        } catch (IOException e) {
            throw new TrackerException(exceptionMsg, e);
        }
    }

    static public String encode(byte[] data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (BencodeOutputStream bencoder = new BencodeOutputStream(out)) {
                bencoder.write(data);
                return out.toString();
            }
        } catch (IOException e) {
            throw new TrackerException(exceptionMsg, e);
        }
    }

    static public String encode(byte[] data, int offset, int length) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (BencodeOutputStream bencoder = new BencodeOutputStream(out)) {
                bencoder.write(data, offset, length);
                return out.toString();
            }
        } catch (IOException e) {
            throw new TrackerException(exceptionMsg, e);
        }
    }

    static public String encode(String data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (BencodeOutputStream bencoder = new BencodeOutputStream(out)) {
                bencoder.writeString(data);
                return out.toString();
            }
        } catch (IOException e) {
            throw new TrackerException(exceptionMsg, e);
        }
    }

    static public <E> String encode(List<E> data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (BencodeOutputStream bencoder = new BencodeOutputStream(out)) {
                bencoder.writeList(data);
                return out.toString();
            }
        } catch (IOException e) {
            throw new TrackerException(exceptionMsg, e);
        }
    }
}
