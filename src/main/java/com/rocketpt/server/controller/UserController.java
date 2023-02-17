package com.rocketpt.server.controller;

import com.rocketpt.server.common.authz.RequiresPermissions;
import com.rocketpt.server.dto.entity.User;
import com.rocketpt.server.sys.service.OrganizationService;
import com.rocketpt.server.sys.service.UserService;
import com.rocketpt.server.sys.dto.PageDTO;

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

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author plexpt
 */
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/users")
public class UserController {

    private final OrganizationService organizationService;
    private final UserService userService;

    public UserController(OrganizationService organizationService, UserService userService) {
        this.organizationService = organizationService;
        this.userService = userService;
    }

    @RequiresPermissions("user:view")
    @GetMapping
    public ResponseEntity<PageDTO<User>> findUsers(Pageable pageable, User user) {
        return ResponseEntity.ok(userService.findUsers(pageable, user));
    }

    @RequiresPermissions("user:create")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest request) {
        return new ResponseEntity<>(
                userService.createUser(
                        request.username(), request.fullName(), request.avatar(), request.gender(),
                        request.email, User.State.NORMAL, request.organizationId()),
                HttpStatus.CREATED);
    }

    @RequiresPermissions("user:update")
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId,
                                           @RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request.fullName(),
                request.avatar(), request.gender(), User.State.NORMAL, request.organizationId()));
    }

    @RequiresPermissions("user:update")
    @PostMapping("/{userId}:lock")
    public ResponseEntity<User> lockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.lockUser(userId));
    }

    @RequiresPermissions("user:update")
    @PostMapping("/{userId}:unlock")
    public ResponseEntity<User> unlockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.unlockUser(userId));
    }

    @RequiresPermissions("user:delete")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    record CreateUserRequest(@NotBlank String username, @NotBlank String fullName,
                             @NotNull User.Gender gender, String email,
                             @NotBlank String avatar, Long organizationId) {
    }

    record UpdateUserRequest(@NotBlank String fullName, @NotNull User.Gender gender,
                             @NotBlank String avatar, Long organizationId) {
    }

}
