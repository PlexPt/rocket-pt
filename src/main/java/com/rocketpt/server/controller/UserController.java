package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.sys.PageDTO;
import com.rocketpt.server.service.sys.OrganizationService;
import com.rocketpt.server.service.sys.UserService;

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

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@SecurityRequirement(name = "bearerAuth")
@RestController
@Tag(name = "用户相关", description = Constants.FinishStatus.FINISHED)
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final OrganizationService organizationService;
    private final UserService userService;


    @SaCheckPermission("user:view")
    @GetMapping
    public ResponseEntity<PageDTO<UserEntity>> findUsers(Pageable pageable, UserEntity userEntity) {
        return ResponseEntity.ok(userService.findUsers(pageable, userEntity));
    }

    @SaCheckPermission("user:create")
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody @Valid CreateUserRequest request) {
        return new ResponseEntity<>(
                userService.createUser(
                        request.username(), request.fullName(), request.avatar(), request.gender(),
                        request.email, UserEntity.State.NORMAL, request.organizationId()),
                HttpStatus.CREATED);
    }

    @SaCheckPermission("user:update")
    @PutMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long userId,
                                                 @RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request.fullName(),
                request.avatar(), request.gender(), UserEntity.State.NORMAL, request.organizationId()));
    }

    @SaCheckPermission("user:update")
    @PostMapping("/{userId}:lock")
    public ResponseEntity<UserEntity> lockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.lockUser(userId));
    }

    @SaCheckPermission("user:update")
    @PostMapping("/{userId}:unlock")
    public ResponseEntity<UserEntity> unlockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.unlockUser(userId));
    }

    @SaCheckPermission("user:delete")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    record CreateUserRequest(@NotBlank String username, @NotBlank String fullName,
                             @NotNull UserEntity.Gender gender, String email,
                             @NotBlank String avatar, Long organizationId) {
    }

    record UpdateUserRequest(@NotBlank String fullName, @NotNull UserEntity.Gender gender,
                             @NotBlank String avatar, Long organizationId) {
    }

}
