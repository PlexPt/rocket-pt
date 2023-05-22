package com.rocketpt.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.dto.entity.MenuEntity;
import com.rocketpt.server.dto.entity.RoleEntity;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.entity.UserRoleEntity;
import com.rocketpt.server.dto.sys.RoleDTO;
import com.rocketpt.server.service.sys.MenuService;
import com.rocketpt.server.service.sys.RoleService;
import com.rocketpt.server.service.sys.UserRoleService;
import com.rocketpt.server.service.sys.UserService;

import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统角色相关", description = Constants.FinishStatus.FINISHED)
@RequestMapping("/roles")
public class RoleController {

    private final UserService userService;
    private final RoleService roleService;
    private final MenuService menuService;
    private final UserRoleService userRoleService;


    @SaCheckPermission("role:view")
    @GetMapping
    public Result findRoles() {
        List<RoleDTO> roles = roleService.findRoles();
        return Result.ok(roles);
    }

    @SaCheckPermission("role:view")
    @GetMapping("/{roleId}/users")
    public Result findRoleUsers(@PathVariable Long roleId,
                                Pageable pageable) {
        return Result.ok(roleService.findRoleUsers(roleId, pageable));
    }

    @SaCheckPermission("role:create")
    @PostMapping
    public Result createRole(@RequestBody @Valid RoleRequest request) {
        RoleEntity role = roleService.createRole(request.name(), request.description());
        return Result.ok(role);
    }

    @SaCheckPermission("role:update")
    @PutMapping("/{roleId}/resources")
    public Result changeResources(@PathVariable Long roleId,
                                  @RequestBody @Valid RoleResourceRequest request) {
        Set<MenuEntity> resources = menuService.findMenuByIds(request.resourceIds());
        return Result.ok(roleService.changeMenu(roleId, resources));
    }

    @SaCheckPermission("role:update")
    @PutMapping("/{roleId}/users")
    public Result changeUsers(@PathVariable Long roleId,
                              @RequestBody @Valid RoleUserRequest request) {
        List<UserEntity> users = userService.findUserByIds(request.userIds());
        return Result.ok(roleService.changeUsers(roleId, users));
    }

    @SaCheckPermission("role:update")
    @PostMapping("/addUser")
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

    @SaCheckPermission("role:update")
    @PostMapping("/removeUser")
    public Result removeUser(@RequestBody @Validated UserRoleEntity request) {
        userRoleService.remove(new QueryWrapper<UserRoleEntity>()
                .lambda()
                .eq(UserRoleEntity::getRoleId, request.getRoleId())
                .eq(UserRoleEntity::getUserId, request.getUserId())
        );

        //FIXME 可能存在权限泄漏

        return Result.ok();
    }

    @SaCheckPermission("role:update")
    @PutMapping("/{roleId}")
    public Result updateRole(@PathVariable Long roleId,
                             @RequestBody @Valid RoleRequest request) {
        return Result.ok(roleService.updateRole(roleId, request.name(),
                request.description()));
    }

    @SaCheckPermission("role:delete")
    @DeleteMapping("/{roleId}")
    public Result deleteRole(@PathVariable Long roleId) {
        roleService.deleteRoleById(roleId);
        return Result.ok();
    }

    record RoleUserRequest(List<Long> userIds) {
    }

    record RoleResourceRequest(Set<Long> resourceIds) {
    }

    record RoleRequest(@NotBlank String name, String description) {
    }

    record RoleAddUserRequest(@NotNull Integer userId, Integer roleId) {
    }

}
