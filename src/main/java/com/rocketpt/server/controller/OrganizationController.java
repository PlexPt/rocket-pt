package com.rocketpt.server.controller;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dto.entity.OrganizationEntity;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.sys.OrgTreeDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import static com.rocketpt.server.dto.entity.OrganizationEntity.Type;

/**
 * @author plexpt
 */
@SecurityRequirement(name = "bearerAuth")
@RestController
@Tag(name = "系统组织相关", description = Constants.FinishStatus.FINISHED)
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final UserService userService;
    private final OrganizationService organizationService;

    @SaCheckPermission("user:view")
    @GetMapping("/tree")
    public ResponseEntity<List<OrgTreeDTO>> findOrgTree(Long parentId) {
        return ResponseEntity.ok(organizationService.findOrgTree(parentId));
    }

    @SaCheckPermission("user:view")
    @GetMapping("/{organizationId}/users")
    public ResponseEntity<PageDTO<UserEntity>> findOrgUsers(Pageable pageable, @RequestParam(required =
            false) String username, @RequestParam(required = false) UserEntity.State state,
                                                            @PathVariable Long organizationId) {
        OrganizationEntity organizationEntity = organizationService.findOrganization(organizationId);
        return ResponseEntity.ok(userService.findOrgUsers(pageable, username, state, organizationEntity));
    }

    @SaCheckPermission("organization:create")
    @PostMapping
    public ResponseEntity<OrganizationEntity> createOrganization(@RequestBody @Valid OrganizationRequest request) {
        return new ResponseEntity<>(organizationService.createOrganization(request.name(),
                request.type(), request.parentId()), HttpStatus.CREATED);
    }

    @SaCheckPermission("organization:update")
    @PutMapping("/{organizationId}")
    public ResponseEntity<OrganizationEntity> updateOrganization(@PathVariable Long organizationId,
                                                                 @RequestBody @Valid OrganizationRequest request) {
        return ResponseEntity.ok(organizationService.updateOrganization(organizationId,
                request.name()));
    }

    @SaCheckPermission("organization:delete")
    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long organizationId) {
        OrganizationEntity organizationEntity = organizationService.findOrganization(organizationId);
        if (userService.existsUsers(organizationEntity)) {
            throw new UserException(CommonResultStatus.FAIL, "节点存在用户，不能删除");
        }
        organizationService.deleteOrganization(organizationId);
        return ResponseEntity.noContent().build();
    }

    record OrganizationRequest(@NotBlank String name, @NotNull Type type, @NotNull Long parentId) {

    }


}
