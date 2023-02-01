package com.rocketpt.server.controller;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.authz.RequiresPermissions;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dto.entity.Organization;
import com.rocketpt.server.dto.entity.User;
import com.rocketpt.server.sys.service.OrganizationService;
import com.rocketpt.server.sys.service.UserService;
import com.rocketpt.server.sys.dto.OrgTreeDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.rocketpt.server.dto.entity.Organization.Type;

/**
 * @author plexpt
 */
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final UserService userService;
    private final OrganizationService organizationService;

    public OrganizationController(UserService userService,
                                  OrganizationService organizationService) {
        this.userService = userService;
        this.organizationService = organizationService;
    }

    @RequiresPermissions("user:view")
    @GetMapping("/tree")
    public ResponseEntity<List<OrgTreeDTO>> findOrgTree(Long parentId) {
        return ResponseEntity.ok(organizationService.findOrgTree(parentId));
    }

    @RequiresPermissions("user:view")
    @GetMapping("/{organizationId}/users")
    public ResponseEntity<PageDTO<User>> findOrgUsers(Pageable pageable, @RequestParam(required =
            false) String username, @RequestParam(required = false) User.State state,
                                                      @PathVariable Long organizationId) {
        Organization organization = organizationService.findOrganization(organizationId);
        return ResponseEntity.ok(userService.findOrgUsers(pageable, username, state, organization));
    }

    @RequiresPermissions("organization:create")
    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody @Valid OrganizationRequest request) {
        return new ResponseEntity<>(organizationService.createOrganization(request.name(),
                request.type(), request.parentId()), HttpStatus.CREATED);
    }

    @RequiresPermissions("organization:update")
    @PutMapping("/{organizationId}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable Long organizationId,
                                                           @RequestBody @Valid OrganizationRequest request) {
        return ResponseEntity.ok(organizationService.updateOrganization(organizationId,
                request.name()));
    }

    @RequiresPermissions("organization:delete")
    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long organizationId) {
        Organization organization = organizationService.findOrganization(organizationId);
        if (userService.existsUsers(organization)) {
            throw new UserException(CommonResultStatus.FAIL, "节点存在用户，不能删除");
        }
        organizationService.deleteOrganization(organizationId);
        return ResponseEntity.noContent().build();
    }

    record OrganizationRequest(@NotBlank String name, @NotNull Type type, @NotNull Long parentId) {

    }


}
