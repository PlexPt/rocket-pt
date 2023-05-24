package com.rocketpt.server.service.infra;

import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Random;

@Component
public class DefaultCheckCodeManager implements CheckCodeManager {
    @Override
    public String generate(Integer userId, String email) {
        ByteBuffer buf = ByteBuffer.allocate(8 + email.getBytes().length);
        buf.putLong(userId);
        buf.put(email.getBytes());
        buf.flip();
        Random random = new Random(buf.getLong());
        int leftLimit = 48, rightLimit = 122, size = 60;
        return random.ints(size, leftLimit, rightLimit + 1)
                .filter(i -> (i < 91 || i > 96) && (i < 58 || i > 64))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
