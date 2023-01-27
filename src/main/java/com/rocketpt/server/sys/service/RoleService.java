package com.rocketpt.server.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.sys.entity.EntityBase;
import com.rocketpt.server.sys.entity.Resource;
import com.rocketpt.server.sys.entity.Role;
import com.rocketpt.server.sys.entity.RoleResource;
import com.rocketpt.server.sys.entity.User;
import com.rocketpt.server.sys.entity.UserRole;
import com.rocketpt.server.sys.event.RoleCreated;
import com.rocketpt.server.sys.event.RoleDeleted;
import com.rocketpt.server.sys.event.RoleUpdated;
import com.rocketpt.server.sys.repository.RoleRepository;
import com.rocketpt.server.sys.dto.PageDTO;
import com.rocketpt.server.sys.dto.RoleDTO;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@Service
@RequiredArgsConstructor
public class RoleService extends ServiceImpl<RoleRepository, Role> {


    private final RoleRepository roleRepository;
    private final UserRoleService userRoleService;
    private final RoleResourceService roleResourceService;

    public List<RoleDTO> findRoles() {
        return list()
                .stream()
                .map(role -> new RoleDTO(
                        role.getId(),
                        role.getName(),
                        role.getDescription(),
                        role.getAvailable(),
                        role.getResources().stream().map(Resource::getId).toList())
                )
                .toList();
    }

    public Role findRoleById(Long roleId) {
        Role role = getById(roleId);
        if (role == null) {
            throw new RocketPTException(CommonResultStatus.RECORD_NOT_EXIST);
        }
        return role;
    }

    @Transactional(rollbackFor = Exception.class)
    public Role createRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        save(role);
        DomainEventPublisher.instance().publish(new RoleCreated(role));
        return role;
    }

    public Role changeResources(Long roleId, Set<Resource> resources) {
        roleResourceService.remove(new QueryWrapper<RoleResource>()
                .lambda()
                .eq(RoleResource::getRoleId, roleId)
        );
        List<RoleResource> roleResources = resources.stream()
                .map(EntityBase::getId)
                .map(e -> new RoleResource(roleId, e))
                .collect(Collectors.toList());
        roleResourceService.saveBatch(roleResources);

        Role role = findRoleById(roleId);
        role.setResources(resources);
        DomainEventPublisher.instance().publish(new RoleUpdated(role));
        return role;
    }

    public Role changeUsers(Long roleId, Set<User> users) {
        userRoleService.remove(new QueryWrapper<UserRole>()
                .lambda()
                .eq(UserRole::getRoleId, roleId)
        );
        List<UserRole> userRoles = users.stream()
                .map(EntityBase::getId)
                .map(e -> new UserRole(e, roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);

        Role role = findRoleById(roleId);
        role.setUsers(users);
//        updateById(role);
        DomainEventPublisher.instance().publish(new RoleUpdated(role));
        return role;
    }

    @Transactional(rollbackFor = Exception.class)
    public Role updateRole(Long roleId, String name, String description) {
        Role role = findRoleById(roleId);
        role.setName(name);
        role.setDescription(description);
        updateById(role);
        DomainEventPublisher.instance().publish(new RoleUpdated(role));
        return role;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleById(Long roleId) {
        Role role = findRoleById(roleId);
        removeById(roleId);
        DomainEventPublisher.instance().publish(new RoleDeleted(role));
    }

    public PageDTO<User> findRoleUsers(Long roleId, Pageable pageable) {
        List<User> users = roleRepository.findRoleUsers(roleId, pageable);
        //todo page
        return new PageDTO<>(users, 10);
    }
}
