package com.rocketpt.server.sys.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import cn.hutool.core.util.RandomUtil;

/**
 * 简单数字验证码
 */

@Service
public class NumberCaptchaService implements CaptchaService {

    /**
     * 默认过期时间 分钟
     */
    private static final int EXPIRE_CAPTCHA = 10;

    private final Cache<String, String> cache = Caffeine.newBuilder()
            .initialCapacity(16)
            .expireAfterAccess(EXPIRE_CAPTCHA, TimeUnit.MINUTES)
            .build();

    @Override
    public synchronized String create(String id) {
        String number = RandomUtil.randomNumbers(6);
        cache.put(id, number);
        return number;
    }

    @Override
    public void saveCaptcha(String id, String captcha) {

    }

    @Override
    public boolean removeCaptcha(String id) {
        cache.put(id, null);
        return false;
    }

    @Override
    public synchronized boolean verifyCaptcha(String id, String captcha) {
        String cap = cache.getIfPresent(id);
        if (cap != null) {
            if (StringUtils.equalsIgnoreCase(captcha, cap)) {
                cache.put(id, null);
                return true;
            }
        }
        return false;
    }
}
