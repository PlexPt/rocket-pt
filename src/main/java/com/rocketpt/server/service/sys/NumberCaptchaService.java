package com.rocketpt.server.service.sys;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.code.kaptcha.Producer;
import com.rocketpt.server.common.exception.RocketPTException;

import com.rocketpt.server.service.SysConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
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
@Primary
@RequiredArgsConstructor
public class NumberCaptchaService implements CaptchaService {
    final Producer captchaProducer;
    final SysConfigService sysConfigService;

    /**
     * 默认过期时间 分钟
     */
    private static final int EXPIRE_CAPTCHA = 10;

    //TODO 换成redis缓存
    private final Cache<String, String> cache = Caffeine.newBuilder()
            .initialCapacity(16)
            .expireAfterWrite(EXPIRE_CAPTCHA, TimeUnit.MINUTES)
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
        cache.invalidate(id);
        return false;
    }

    @Override
    public synchronized boolean verifyCaptcha(String id, String captcha) {

        //验证码开关
        if (!sysConfigService.isCaptchaEnable()) {
            return true;
        }

        String cap = cache.getIfPresent(id);
        if (cap != null) {
            if (StringUtils.equalsIgnoreCase(captcha, cap)) {
                cache.invalidate(id);
                return true;
            }
        }
        return false;
    }
}
