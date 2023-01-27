package com.rocketpt.server.controller;

import com.rocketpt.server.common.authz.RequiresPermissions;
import com.rocketpt.server.sys.entity.Resource;
import com.rocketpt.server.sys.entity.Role;
import com.rocketpt.server.sys.entity.User;
import com.rocketpt.server.sys.service.ResourceService;
import com.rocketpt.server.sys.service.RoleService;
import com.rocketpt.server.sys.service.UserService;
import com.rocketpt.server.sys.dto.PageDTO;
import com.rocketpt.server.sys.dto.RoleDTO;

import org.springframework.data.domain.Pageable;
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
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * @author plexpt
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

    private final UserService userService;
    private final RoleService roleService;
    private final ResourceService resourceService;

    public RoleController(UserService userService, RoleService roleService,
                          ResourceService resourceService) {
        this.userService = userService;
        this.roleService = roleService;
        this.resourceService = resourceService;
    }

    @RequiresPermissions("role:view")
    @GetMapping
    public ResponseEntity<List<RoleDTO>> findRoles() {
        return ResponseEntity.ok(roleService.findRoles());
    }

    @RequiresPermissions("role:view")
    @GetMapping("/{roleId}/users")
    public ResponseEntity<PageDTO<User>> findRoleUsers(@PathVariable Long roleId,
                                                       Pageable pageable) {
        return ResponseEntity.ok(roleService.findRoleUsers(roleId, pageable));
    }

    @RequiresPermissions("role:create")
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody @Valid RoleRequest request) {
        return new ResponseEntity<>(roleService.createRole(request.name(), request.description())
                , HttpStatus.CREATED);
    }

    @RequiresPermissions("role:update")
    @PutMapping("/{roleId}/resources")
    public ResponseEntity<Role> changeResources(@PathVariable Long roleId,
                                                @RequestBody @Valid RoleResourceRequest request) {
        Set<Resource> resources = resourceService.findResourceByIds(request.resourceIds());
        return ResponseEntity.ok(roleService.changeResources(roleId, resources));
    }

    @RequiresPermissions("role:update")
    @PutMapping("/{roleId}/users")
    public ResponseEntity<Role> changeUsers(@PathVariable Long roleId,
                                            @RequestBody @Valid RoleUserRequest request) {
        Set<User> users = userService.findUserByIds(request.userIds());
        return ResponseEntity.ok(roleService.changeUsers(roleId, users));
    }

    @RequiresPermissions("role:update")
    @PutMapping("/{roleId}")
    public ResponseEntity<Role> updateRole(@PathVariable Long roleId,
                                           @RequestBody @Valid RoleRequest request) {
        return ResponseEntity.ok(roleService.updateRole(roleId, request.name(),
                request.description()));
    }

    @RequiresPermissions("role:delete")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRoleById(roleId);
        return ResponseEntity.noContent().build();
    }

    record RoleUserRequest(Set<Long> userIds) {
    }

    record RoleResourceRequest(Set<Long> resourceIds) {
    }

    record RoleRequest(@NotBlank String name, String description) {
    }

}
