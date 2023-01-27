package com.rocketpt.server.infra;

import com.rocketpt.server.common.EventStore;
import com.rocketpt.server.sys.service.SessionService;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final SessionService sessionService;
    private final EventStore eventStore;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration loginInterceptor =
                registry.addInterceptor(new AuthInterceptor(sessionService));
        loginInterceptor.addPathPatterns("/**");
        loginInterceptor.excludePathPatterns(
                "/login",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/assets/**",
                "/favicon.ico",
                "/avatar.jpg",
                "/index.html",
                "/"
        );
        InterceptorRegistration eventSubscribesInterceptor =
                registry.addInterceptor(new EventSubscribesInterceptor(eventStore, sessionService));
        eventSubscribesInterceptor.addPathPatterns("/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources" +
                        "/webjars/admin3-ui/");
    }

//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
//        return builder -> builder.serializers(EntityBaseSerializer.instance);
//    }
}
