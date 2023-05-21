package com.rocketpt.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.dto.entity.User;
import com.rocketpt.server.sys.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzr
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {

    private final UserService userService;

    @GetMapping("/{checkCode}")
    public Result validate(@PathVariable String checkCode) {
        Optional<User> optional = Optional.ofNullable(userService.getOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getCheckCode, checkCode)
                        .eq(User::getState, User.State.INACTIVATED)
        ));
        if (optional.isEmpty()) {
            return Result.notFound();
        }
        User user = optional.get();
        user.setState(User.State.NORMAL);
        userService.updateById(user);
        return Result.ok();
    }

}
