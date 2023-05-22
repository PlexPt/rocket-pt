package com.rocketpt.server.config;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    public static final String KEY_CACHE_MANAGER = "keyCacheManager";

    @Bean
    @Primary
    public CacheManager cacheManager() {

        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.SECONDS);
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

    @Bean(KEY_CACHE_MANAGER)
    public CacheManager keyCacheManager() {

        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(10)
                // 缓存的最大条数
                .maximumSize(10)
                .expireAfterWrite(60, TimeUnit.SECONDS);
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

}
