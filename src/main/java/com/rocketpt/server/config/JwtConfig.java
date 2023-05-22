package com.rocketpt.server.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.stp.StpLogic;

@Configuration
public class JwtConfig {

    // Sa-Token 整合 jwt (Stateless 无状态模式)
//    @Bean
//    public StpLogic getStpLogicJwt() {
//        return new StpLogicJwtForStateless();
//    }
}
