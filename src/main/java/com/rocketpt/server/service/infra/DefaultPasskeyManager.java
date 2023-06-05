package com.rocketpt.server.service.infra;

import org.springframework.stereotype.Component;

import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultPasskeyManager implements PasskeyManager {

    @Override
    public String generate(Integer userId) {
        //32位UUID
        String passkey = UUID.fastUUID().toString(true);

        return passkey;
    }
}
