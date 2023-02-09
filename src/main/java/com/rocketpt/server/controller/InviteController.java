package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Res;
import com.rocketpt.server.dto.param.InviteParam;
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
@RequiredArgsConstructor
@Tag(name = "邀请相关", description = Constants.FinishStatus.FINISHED)
@RequestMapping("/invite")
public class InviteController {


    private final UserService userService;

    @PostMapping("send")
    public Res register(@RequestBody @Validated InviteParam param) {

        return Res.ok();

    }

}
