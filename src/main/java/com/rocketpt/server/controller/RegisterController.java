package com.rocketpt.server.controller;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.param.RegisterParam;
import com.rocketpt.server.service.sys.CaptchaService;
import com.rocketpt.server.service.sys.UserService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author plexpt
 */
@Slf4j
@RestController
@Tag(name = "用户注册", description = Constants.FinishStatus.FINISHED)
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final CaptchaService captchaService;

    private final UserService userService;

    @PostMapping
    public Result register(@RequestBody @Validated RegisterParam param) {
        if (!captchaService.verifyCaptcha(param.getUuid(), param.getCode())) {
            throw new RocketPTException("验证码不正确");
        }
        if (param.getType() != 1 && param.getCode().isEmpty()) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR);
        }
        userService.register(param);

        return Result.ok();
    }

}
