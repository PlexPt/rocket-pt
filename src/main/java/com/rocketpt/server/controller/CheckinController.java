package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.service.sys.UserService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@Tag(name = "用户签到相关", description = Constants.FinishStatus.UNFINISHED)
@RequiredArgsConstructor
@RequestMapping("/checkin")
@Validated
public class CheckinController {


    private final UserService userService;


    @Operation(summary = "签到详情", description = "判断今日是否签到过，如果已经签到过，则返回签到详情")
    @GetMapping("/info")
    public Result info() {
        //TODO 签到详情

        return Result.ok();
    }

    @Operation(summary = "签到")
    @PostMapping("/checkin")
    public Result checkin() {
        //TODO 签到 （可考虑动态验证码。也可以不加验证码方面自动签到）

        return Result.ok();
    }
}
