package com.rocketpt.server.sys.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.code.kaptcha.Producer;
import com.rocketpt.server.common.exception.RocketPTException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;

import static com.rocketpt.server.common.CommonResultStatus.FAIL;

/**
 * 简单数字验证码
 */

@Service
@RequiredArgsConstructor
public class NumberCaptchaService implements CaptchaService {
    private final Producer captchaProducer;

    /**
     * 默认过期时间 分钟
     */
    private static final int EXPIRE_CAPTCHA = 10;

    private final Cache<String, String> cache = Caffeine.newBuilder()
            .initialCapacity(16)
            .expireAfterAccess(EXPIRE_CAPTCHA, TimeUnit.MINUTES)
            .build();

    @Override
    public synchronized String create(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            throw new RocketPTException(FAIL, "uuid不能为空");
        }
        String number = RandomUtil.randomNumbers(6);
        cache.put(uuid, number);
        return number;
    }

    @Override
    public BufferedImage getCaptcha(String code) {

        return captchaProducer.createImage(code);
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
