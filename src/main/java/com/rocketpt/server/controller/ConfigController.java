package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.service.mail.MailConfiger;
import com.rocketpt.server.service.mail.SmtpConfig;
import com.rocketpt.server.service.sys.SystemConfigService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name = "分别的系统配置", description = Constants.FinishStatus.FINISHED)
@RequestMapping("/sys/config/part")
public class ConfigController {


    final SystemConfigService sysConfigService;

    final MailConfiger mailConfiger;

    @Operation(summary = "修改邮件服务器配置")
    @SaCheckPermission("sys:config:edit")
    @PostMapping("/mail")
    public Result updateSmtpConfig(@RequestBody SmtpConfig config) {
        sysConfigService.set("smtp_config", config);

        mailConfiger.changeConfig(config);

        return Result.ok();
    }

}
