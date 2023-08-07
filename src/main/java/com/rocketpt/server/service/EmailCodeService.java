package com.rocketpt.server.service;


import cn.hutool.core.util.RandomUtil;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.service.mail.MailService;
import com.rocketpt.server.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailCodeService {
    final MailService mailService;
    final RedisUtil redisUtil;

    public void setMailCode(String email) {
        String confirmCode = RandomUtil.randomNumbers(6);
        String key = "emailConfirmCode:" + email + ":code";
        String time = "emailConfirmCode:" + email + ":time";
        redisUtil.setEx(key, confirmCode, 30, TimeUnit.MINUTES);
        redisUtil.setEx(time, "0", 30, TimeUnit.MINUTES);
        String text = I18nMessage.getMessage("confirm_email") + confirmCode;
        mailService.sendMail(email,
                I18nMessage.getMessage("confirm_title"),
                text,
                null);
    }

    public void checkEmailCode(String email, String code) {
        //  检查邮箱验证码是否正确 正确才能继续注册 否则抛出
        String key = "emailConfirmCode:" + email + ":code";
        String timeKey = "emailConfirmCode:" + email + ":time";
        String codeSaved = redisUtil.get(key);
        if (StringUtils.isEmpty(codeSaved)) {
            throw new RocketPTException("邮箱验证码已失效，请重新发送");
        }
        int timesUsed = 10;
        try {
            String times = redisUtil.get(timeKey);
            timesUsed = Integer.parseInt(times);
        } catch (Exception e) {
        }

        if (timesUsed > 5) {
            resetEmailCodeAttempts(key, timeKey);

            throw new RocketPTException("邮箱验证码达到最大次数，请重新发送");
        }

        if (!StringUtils.equals(codeSaved, code)) {

            timesUsed++;

            redisUtil.setEx(timeKey, "" + timesUsed, 30, TimeUnit.MINUTES);

            throw new RocketPTException("邮箱验证码不正确");
        }

        resetEmailCodeAttempts(key, timeKey);
    }

    private void resetEmailCodeAttempts(String key, String timeKey) {
        redisUtil.delete(key);
        redisUtil.delete(timeKey);
    }

}

