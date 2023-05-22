package com.rocketpt.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.dev33.satoken.secure.SaSecureUtil;

@SpringBootTest
class RocketServerApplicationTests {

    @Test
    void contextLoads() {
        // sha256加密
        // feb7b5e649bdea855a6d8ff1c4eefd82e6c4bb5d90712ef387774adc532f3dcc
        // rocketpt 6a2757026e702f1ca87ccca0522080cf6e722760de88c841686d2261c243cc96
        System.out.println(SaSecureUtil.sha256("rocketpt" + "123456"));
    }

}
