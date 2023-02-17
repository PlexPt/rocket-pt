package com.rocketpt.server.controller;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.base.Res;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.param.RegisterParam;
import com.rocketpt.server.sys.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {


    private final UserService userService;

    @PostMapping
    public Res register(@RequestBody @Validated RegisterParam param) {
        if (param.getType() != 1 && param.getCode().isEmpty()) throw new RocketPTException(CommonResultStatus.PARAM_ERROR);
        userService.register(param);
        return Res.ok();
    }

}
