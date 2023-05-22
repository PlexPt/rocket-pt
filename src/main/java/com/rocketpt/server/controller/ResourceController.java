package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.dto.entity.MenuEntity;
import com.rocketpt.server.service.sys.MenuService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@Tag(name = "系统菜单资源相关", description = Constants.FinishStatus.FINISHED)
@RequestMapping("/resources")
public class ResourceController {

    private final MenuService menuService;


    @GetMapping("/menu")
    public Result findMenus() {
        return Result.ok(menuService.findMenus());
    }

    @GetMapping("/permission")
    public Result permission() {
        List<String> list = StpUtil.getPermissionList();
        return Result.ok(list);
    }

    @SaCheckPermission("resource:view")
    @GetMapping("/tree")
    public Result findResourceTree() {
        return Result.ok(menuService.findResourceTree());
    }

    @SaCheckPermission("resource:create")
    @PostMapping
    public Result createResource(@RequestBody ResourceRequest request) {
        MenuEntity menu = menuService.createResource(request.name(), request.type()
                , request.url(), request.icon(), request.permission(), request.parentId());
        return Result.ok(menu);
    }

    @SaCheckPermission("resource:update")
    @PutMapping("/{resourceId}")
    public Result updateResource(@PathVariable Long resourceId,
                                 @RequestBody ResourceRequest request) {
        MenuEntity entity = menuService.updateResource(resourceId, request.name(),
                request.type(), request.url(), request.icon(), request.permission(),
                request.parentId());
        return Result.ok(entity);
    }

    @SaCheckPermission("resource:delete")
    @DeleteMapping("/{resourceId}")
    public Result deleteResource(@PathVariable Long resourceId) {
        menuService.deleteResourceById(resourceId);
        return Result.ok();
    }


    record ResourceRequest(@NotBlank String name,
                           @NotNull MenuEntity.Type type,
                           String url,
                           String icon,
                           @NotBlank String permission,
                           @NotNull Long parentId) {

    }

}
