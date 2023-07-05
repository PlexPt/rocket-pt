package com.rocketpt.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.dev33.satoken.secure.SaSecureUtil;

@SpringBootTest
class RocketServerApplicationTests {

    @Test
    void contextLoads() {
        // sha256加盐生成密码
        //前面是密码 后面是盐值
        //如密码 rocketpt + 盐 123456
        // 结果是：6a2757026e702f1ca87ccca0522080cf6e722760de88c841686d2261c243cc96
        System.out.println(SaSecureUtil.sha256("rocketpt" + "123456"));
    }

}
