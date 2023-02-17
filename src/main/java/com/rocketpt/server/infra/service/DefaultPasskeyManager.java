package com.rocketpt.server.infra.service;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.SessionItemHolder;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.sys.dto.UserinfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultPasskeyManager implements PasskeyManager {

    @Override
    public String generate(long userId) {
        try {
            UserinfoDTO userinfoDTO = (UserinfoDTO) SessionItemHolder.getItem(Constants.SESSION_CURRENT_USER);
            String token = userinfoDTO.token();
            MessageDigest md = MessageDigest.getInstance("MD5");
            ByteBuffer buf = ByteBuffer.allocate(16 + token.getBytes().length);
            buf.putLong(userId);
            buf.putLong(new Date().getTime());
            buf.put(token.getBytes());
            buf.flip();
            md.update(buf);
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RocketPTException(CommonResultStatus.SERVER_ERROR);
        }
    }
}
