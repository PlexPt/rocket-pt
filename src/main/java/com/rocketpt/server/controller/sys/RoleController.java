package com.rocketpt.server.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.OrderPageParam;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.dto.entity.RoleEntity;
import com.rocketpt.server.dto.entity.UserRoleEntity;
import com.rocketpt.server.dto.param.RoleUserParam;
import com.rocketpt.server.service.sys.RoleService;
import com.rocketpt.server.service.sys.UserRoleService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统角色相关", description = Constants.FinishStatus.FINISHED)
@RequestMapping("/roles")
public class RoleController {

    final RoleService roleService;
    final UserRoleService userRoleService;


    @Schema(description = "角色列表")
    @SaCheckPermission("role:view")
    @GetMapping
    public Result findRoles() {
        List<RoleEntity> roles = roleService.findRoles();
        return Result.ok(roles);
    }

    @Schema(description = "查看角色用户列表-分页")
    @SaCheckPermission("role:view")
    @PostMapping("/{roleId}/users")
    public Result findRoleUsers(@PathVariable Integer roleId,
                                @RequestBody @Validated OrderPageParam param) {

        return roleService.findRoleUsers(roleId, param);
    }

    @Schema(description = "新增角色")
    @SaCheckPermission("role:edit")
    @PostMapping("/add")
    public Result createRole(@RequestBody @Validated RoleEntity entity) {
        roleService.createRole(entity);
        return Result.ok();
    }

    @Schema(description = "编辑角色")
    @SaCheckPermission("role:edit")
    @PostMapping("/update")
    public Result changeResources(@RequestBody @Valid RoleEntity entity) {
        roleService.changeResources(entity);

        return Result.ok();
    }

    @Schema(description = "调整角色用户")
    @SaCheckPermission("role:edit")
    @PostMapping("/user/update")
    public Result changeUsers(@RequestBody @Valid RoleUserParam param) {
        roleService.changeUsers(param);
        return Result.ok();
    }

    @Schema(description = "为角色添加某个用户")
    @SaCheckPermission("role:edit")
    @PostMapping("/user/add")
    public Result addUser(@RequestBody @Validated UserRoleEntity request) {
        long count = userRoleService.count(new QueryWrapper<UserRoleEntity>()
                .lambda()
                .eq(UserRoleEntity::getRoleId, request.getRoleId())
                .eq(UserRoleEntity::getUserId, request.getUserId())
        );
        //FIXME 可能存在权限泄漏
        if (count == 0) {
            userRoleService.save(request);
        }

        return Result.ok();
    }

    @Schema(description = "为角色移除某个用户")
    @SaCheckPermission("role:edit")
    @PostMapping("/user/remove")
    public Result removeUser(@RequestBody @Validated UserRoleEntity request) {
        userRoleService.remove(new QueryWrapper<UserRoleEntity>()
                .lambda()
                .eq(UserRoleEntity::getRoleId, request.getRoleId())
                .eq(UserRoleEntity::getUserId, request.getUserId())
        );

        //FIXME 可能存在权限泄漏

        return Result.ok();
    }

    @Schema(description = "删除角色")
    @SaCheckPermission("role:edit")
    @PostMapping("/remove/{roleId}")
    public Result deleteRole(@PathVariable Integer roleId) {
        roleService.deleteRoleById(roleId);
        return Result.ok();
    }


}
