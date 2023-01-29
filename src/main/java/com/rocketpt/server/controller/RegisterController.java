package com.rocketpt.server.controller;

import com.rocketpt.server.common.base.Res;
import com.rocketpt.server.controller.param.RegisterParam;
import com.rocketpt.server.sys.service.UserService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {


    private final UserService userService;

    @PostMapping
    public Res register(@RequestBody @Validated RegisterParam param) {
        boolean success = userService.register(param);
        if (success) {
            return Res.ok("Success");
        } else {
            return Res.error("Error registering user");
        }
    }

}
