package com.rocketpt.server.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.dev33.satoken.interceptor.SaInterceptor;
import lombok.RequiredArgsConstructor;

//@Configuration
@RequiredArgsConstructor
public class SaTokenConfigure implements WebMvcConfigurer {


    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/isLogin")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/login/**")
                .excludePathPatterns("/user/reg/**")
                .excludePathPatterns("/pay/alipay/notify")
                .excludePathPatterns("/pay/alipay/notify/**")
                .excludePathPatterns("/pay/wxpay/notify")
                .excludePathPatterns("/pay/wxpay/notify/**")

        ;
    }

}
