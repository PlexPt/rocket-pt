package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dao.RoleDao;
import com.rocketpt.server.dto.entity.MenuEntity;
import com.rocketpt.server.dto.entity.RoleEntity;
import com.rocketpt.server.dto.entity.RoleMenuEntity;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.entity.UserRoleEntity;
import com.rocketpt.server.dto.event.RoleCreated;
import com.rocketpt.server.dto.event.RoleUpdated;
import com.rocketpt.server.dto.sys.PageDTO;
import com.rocketpt.server.dto.sys.RoleDTO;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

/**
 * 角色
 *
 * @author plexpt
 */
@Service
@RequiredArgsConstructor
public class RoleService extends ServiceImpl<RoleDao, RoleEntity> {


    private final UserRoleService userRoleService;
    private final RoleMenuService roleMenuService;
    private final UserService userService;

    public List<RoleDTO> findRoles() {


        List<RoleDTO> list = list()
                .stream()
                .map(role -> new RoleDTO(
                        role.getId(),
                        role.getName(),
                        role.getDescription(),
                        true,
                        roleMenuService.getMenuIdsByRoleId(role.getId()))
                )
                .toList();

        return list;
    }

    public RoleEntity findRoleById(Long roleId) {
        RoleEntity roleEntity = getById(roleId);
        if (roleEntity == null) {
            throw new RocketPTException(CommonResultStatus.RECORD_NOT_EXIST);
        }
        return roleEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public RoleEntity createRole(String name, String description) {
        RoleEntity role = new RoleEntity();
        role.setName(name);
        role.setDescription(description);
        save(role);
        DomainEventPublisher.instance().publish(new RoleCreated(role));
        return role;
    }


    public RoleEntity changeMenu(Long roleId, Set<MenuEntity> menuEntities) {
        roleMenuService.remove(new QueryWrapper<RoleMenuEntity>()
                .lambda()
                .eq(RoleMenuEntity::getRoleId, roleId)
        );
        List<RoleMenuEntity> roleMenuEntities = menuEntities.stream()
                .map(MenuEntity::getId)
                .map(menuId -> new RoleMenuEntity(null, roleId, menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuEntities);

        RoleEntity roleEntity = findRoleById(roleId);
        roleEntity.setMenuEntities(menuEntities);
        DomainEventPublisher.instance().publish(new RoleUpdated(roleEntity));
        return roleEntity;
    }

    public RoleEntity changeUsers(Long roleId, List<UserEntity> users) {
        userRoleService.remove(new QueryWrapper<UserRoleEntity>()
                .lambda()
                .eq(UserRoleEntity::getRoleId, roleId)
        );
        List<UserRoleEntity> userRoleEntities = users.stream()
                .map(UserEntity::getId)
                .map(userId -> new UserRoleEntity(null, (long) userId, roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleEntities);

        RoleEntity roleEntity = findRoleById(roleId);
        roleEntity.setUsers(users);
//        updateById(role);
        DomainEventPublisher.instance().publish(new RoleUpdated(roleEntity));
        return roleEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public RoleEntity updateRole(Long roleId, String name, String description) {
        RoleEntity role = findRoleById(roleId);
        role.setName(name);
        role.setDescription(description);
        updateById(role);
//        DomainEventPublisher.instance().publish(new RoleUpdated(role));
        return role;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleById(Long roleId) {
//        RoleEntity role = findRoleById(roleId);
        removeById(roleId);
        roleMenuService.remove(new QueryWrapper<RoleMenuEntity>()
                .lambda()
                .eq(RoleMenuEntity::getRoleId, roleId)
        );
        userRoleService.remove(new QueryWrapper<UserRoleEntity>()
                .lambda()
                .eq(UserRoleEntity::getRoleId, roleId)
        );

//        DomainEventPublisher.instance().publish(new RoleDeleted(roleId));
    }

    public PageDTO<UserEntity> findRoleUsers(Long roleId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());

        List<UserRoleEntity> list = userRoleService.list(Wrappers.<UserRoleEntity>lambdaQuery()
                .eq(UserRoleEntity::getRoleId, roleId)
        );
        List<Long> userids = list.stream()
                .map(UserRoleEntity::getUserId)
                .toList();

        List<UserEntity> users = userService.findUserByIds(userids);

        long total = new PageInfo(users).getTotal();
        return new PageDTO<>(users, total);
    }
}
