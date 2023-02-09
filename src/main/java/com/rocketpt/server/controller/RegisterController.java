package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Res;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.param.RegisterParam;
import com.rocketpt.server.sys.service.CaptchaService;
import com.rocketpt.server.sys.service.UserService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@RestController
@Tag(name = "用户注册", description = Constants.FinishStatus.FINISHED)
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final CaptchaService captchaService;

    private final UserService userService;

    @PostMapping
    public Res register(@RequestBody @Validated RegisterParam param) {

        if (!captchaService.verifyCaptcha(param.getUsername(), param.getCode())) {
            throw new RocketPTException("验证码不正确");
        }

        Long id = userService.register(param);

        if (id > 0) {
            return Res.ok("Success");
        } else {
            return Res.error("Error registering user");
        }
    }

}
