package com.rocketpt.server.service.mail;


import com.rocketpt.server.service.SysConfigService;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Configuration
@Data
@RequiredArgsConstructor
public class MailConfiger implements ApplicationRunner {

    private final JavaMailSenderImpl mailSender;//注入邮件工具类

    private final SysConfigService sysConfigService;//注入邮件工具类


    @Override
    public void run(ApplicationArguments args) {
        SmtpConfig smtpConfig = sysConfigService.getSmtpConfig();
        if (smtpConfig == null) {
            return;
        }
        changeConfig(smtpConfig);
    }


    public void changeConfig(SmtpConfig smtpConfig) {
        mailSender.setHost(smtpConfig.getHost());
        mailSender.setPort(smtpConfig.getPort());
        mailSender.setUsername(smtpConfig.getUsername());
        mailSender.setPassword(smtpConfig.getPassword());
        mailSender.setProtocol(smtpConfig.getProtocol());
    }
}
