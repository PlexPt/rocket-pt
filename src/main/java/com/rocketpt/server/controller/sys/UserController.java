package com.rocketpt.server.controller.sys;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.param.UserParam;
import com.rocketpt.server.service.sys.UserService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@RestController
@Tag(name = "用户相关", description = Constants.FinishStatus.FINISHED)
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @Operation(summary = "用户列表", description = "用户列表")
    @SaCheckPermission("user:view")
    @PostMapping("/list")
    public Result listUsers(@RequestBody @Validated UserParam param) {
        return userService.findUsers(param);
    }


    @Operation(summary = "用户详情")
    @Parameter(name = "userId", description = "用户 ID", required = true, in = ParameterIn.PATH)
    @PostMapping("/{userId}")
    public Result info(@PathVariable Integer userId) {

        UserEntity entity = userService.getById(userId);
        return Result.ok(entity);
    }

    @Operation(summary = "新增用户")
    @SaCheckPermission("user:create")
    @PostMapping("/create")
    public Result createUser(@RequestBody @Valid UserEntity entity) {
        userService.createUser(entity);
        return Result.ok();
    }

    @Operation(summary = "编辑用户")
    @SaCheckPermission("user:update")
    @PostMapping("/update")
    public Result updateUser(@RequestBody @Valid UserEntity entity) {
        userService.updateUser(entity);
        return Result.ok();
    }

    @Operation(summary = "锁定用户")
    @Parameter(name = "userId", description = "用户 ID", required = true, in = ParameterIn.PATH)
    @SaCheckPermission("user:update")
    @PostMapping("/lock/{userId}")
    public Result lockUser(@PathVariable Integer userId) {
        userService.lockUser(userId);

        return Result.ok();
    }

    @Operation(summary = "解锁用户")
    @Parameter(name = "userId", description = "用户 ID", required = true, in = ParameterIn.PATH)
    @SaCheckPermission("user:update")
    @PostMapping("/unlock/{userId}")
    public Result unlockUser(@PathVariable Integer userId) {
        userService.unlockUser(userId);

        return Result.ok();
    }

    @Operation(summary = "删除用户")
    @Parameter(name = "userId", description = "用户 ID", required = true, in = ParameterIn.PATH)
    @SaCheckPermission("user:delete")
    @PostMapping("/remove/{userId}")
    public Result deleteUser(@PathVariable Integer userId) {
        userService.delete(userId);
        return Result.ok();
    }

}
