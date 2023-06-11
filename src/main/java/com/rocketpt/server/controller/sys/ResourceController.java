package com.rocketpt.server.controller.sys;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.dto.entity.ResourceEntity;
import com.rocketpt.server.service.sys.ResourceService;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private final ResourceService resourceService;


    @Operation(summary = "获取权限列表", description = "获取权限列表")
    @GetMapping("/permission")
    public Result permission() {
        List<String> list = StpUtil.getPermissionList();
        return Result.ok(list);
    }


    @Operation(summary = "获取资源列表")
    @GetMapping("/list")
    public Result listResources() {
        return Result.ok(resourceService.listResources());
    }

    @Operation(summary = "获取资源树")
    @SaCheckPermission("resource:view")
    @GetMapping("/tree")
    public Result findResourceTree() {
        return Result.ok(resourceService.findResourceTree());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取单个资源详情")
    @SaCheckPermission("resource:view")
    public Result get(@PathVariable("id") Integer id) {
        ResourceEntity entity = resourceService.getById(id);

        return Result.ok(entity);
    }

    @Operation(summary = "新增资源")
    @SaCheckPermission("resource:edit")
    @PostMapping("/add")
    public Result addResource(@RequestBody ResourceEntity entity) {
        resourceService.createResource(entity);
        return Result.ok();
    }

    @Operation(summary = "修改资源")
    @SaCheckPermission("resource:edit")
    @PutMapping("/update")
    public Result updateResource(@RequestBody ResourceEntity entity) {

        resourceService.updateById(entity);

        return Result.ok();
    }

    @Operation(summary = "删除资源")
    @SaCheckPermission("resource:edit")
    @DeleteMapping("/{resourceId}")
    public Result deleteResource(@PathVariable Long resourceId) {
        resourceService.deleteResourceById(resourceId);
        return Result.ok();
    }

}
