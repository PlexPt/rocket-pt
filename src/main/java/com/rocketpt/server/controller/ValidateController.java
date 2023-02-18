package com.rocketpt.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rocketpt.server.common.base.Res;
import com.rocketpt.server.dto.entity.User;
import com.rocketpt.server.sys.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public Res validate(@PathVariable String checkCode) {
        Optional<User> optional = Optional.ofNullable(userService.getOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getCheckCode, checkCode)
                        .eq(User::getState, User.State.INACTIVATED)
        ));
        if (optional.isEmpty()) return Res.notFound();
        User user = optional.get();
        user.setState(User.State.NORMAL);
        userService.updateById(user);
        return Res.ok();
    }

}
