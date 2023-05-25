package com.rocketpt.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.service.sys.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzr
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "校验相关", description = Constants.FinishStatus.FINISHED)
@RequestMapping("/validate")
public class ValidateController {

    private final UserService userService;

//    @GetMapping("/{checkCode}")
//    public Result validate(@PathVariable String checkCode) {
//        Optional<UserEntity> optional = Optional.ofNullable(userService.getOne(
//                Wrappers.<UserEntity>lambdaQuery()
//                        .eq(UserEntity::getCheckCode, checkCode)
//                        .eq(UserEntity::getState, UserEntity.State.INACTIVATED)
//        ));
//        if (optional.isEmpty()) {
//            return Result.notFound();
//        }
//        UserEntity userEntity = optional.get();
//        userEntity.setState(UserEntity.State.NORMAL.getCode());
//        userService.updateById(userEntity);
//        return Result.ok();
//    }

}
