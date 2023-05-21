package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.SessionItemHolder;
import com.rocketpt.server.common.authz.RequiresPermissions;
import com.rocketpt.server.dto.entity.Resource;
import com.rocketpt.server.dto.sys.MenuResourceDTO;
import com.rocketpt.server.dto.sys.ResourceTreeDTO;
import com.rocketpt.server.dto.sys.UserinfoDTO;
import com.rocketpt.server.sys.service.ResourceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    private final ResourceService resourceService;


    @GetMapping("/menu")
    public ResponseEntity<List<MenuResourceDTO>> findMenus() {
        UserinfoDTO userInfo =
                (UserinfoDTO) SessionItemHolder.getItem(Constants.SESSION_CURRENT_USER);
        return ResponseEntity.ok(resourceService.findMenus(userInfo.permissions()));
    }

    @RequiresPermissions("resource:view")
    @GetMapping("/tree")
    public ResponseEntity<List<ResourceTreeDTO>> findResourceTree() {
        return ResponseEntity.ok(resourceService.findResourceTree());
    }

    @RequiresPermissions("resource:create")
    @PostMapping
    public ResponseEntity<Resource> createResource(@RequestBody ResourceRequest request) {
        return new ResponseEntity<>(resourceService.createResource(request.name(), request.type()
                , request.url(), request.icon(), request.permission(), request.parentId()),
                HttpStatus.CREATED);
    }

    @RequiresPermissions("resource:update")
    @PutMapping("/{resourceId}")
    public ResponseEntity<Resource> updateResource(@PathVariable Long resourceId,
                                                   @RequestBody ResourceRequest request) {
        return ResponseEntity.ok(resourceService.updateResource(resourceId, request.name(),
                request.type(), request.url(), request.icon(), request.permission(),
                request.parentId()));
    }

    @RequiresPermissions("resource:delete")
    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long resourceId) {
        resourceService.deleteResourceById(resourceId);
        return ResponseEntity.noContent().build();
    }


    record ResourceRequest(@NotBlank String name,
                           @NotNull Resource.Type type,
                           String url,
                           String icon,
                           @NotBlank String permission,
                           @NotNull Long parentId) {

    }

}
