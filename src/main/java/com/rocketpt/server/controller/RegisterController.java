package com.rocketpt.server.controller;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.param.RegisterCodeParam;
import com.rocketpt.server.dto.param.RegisterParam;
import com.rocketpt.server.service.sys.CaptchaService;
import com.rocketpt.server.service.sys.UserService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
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

    final CaptchaService captchaService;

    final UserService userService;

    @Operation(summary = "注册")
    @PostMapping
    public Result register(@RequestBody @Validated RegisterParam param) {

        userService.register(param);

        return Result.ok();
    }

//    /**
//     * @return secret
//     */
//    @Operation(summary = "注册确认")
//    @Parameter(name = "code", description = "邮件里面的code", required = true, in = ParameterIn.PATH)
//    @PostMapping("/confirm/{code}")
//    public Result confirm(@PathVariable @NotBlank @Validated String code) {
//        userService.confirm(code);
//
//        return Result.ok();
//    }

    /**
     * @return secret
     */
    @Operation(summary = "注册-发送邮箱验证码")
    @PostMapping("/send")
    public Result sendCode(@Validated @RequestBody RegisterCodeParam param) {
        userService.sendRegCode(param);

        return Result.ok();
    }
}
