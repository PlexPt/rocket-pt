package com.rocketpt.server.config;

import com.rocketpt.server.common.EventStore;
import com.rocketpt.server.service.sys.SessionService;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */


@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final EventStore eventStore;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        InterceptorRegistration loginInterceptor =
//                registry.addWebRequestInterceptor()
//        loginInterceptor.addPathPatterns("/**");
//        loginInterceptor.excludePathPatterns(
//                "/login",
//                "/register",
//                "/validate/**",
//                "/swagger-ui.html",
//                "/swagger-ui/**",
//                "/v3/api-docs/**",
//                "/assets/**",
//                "/favicon.ico",
//                "/avatar.jpg",
//                "/code.jpg",
//                "/index.html",
//                "/"
//        );
        InterceptorRegistration eventSubscribesInterceptor =
                registry.addInterceptor(new EventSubscribesInterceptor(eventStore ));
        eventSubscribesInterceptor.addPathPatterns("/**");
    }

}
