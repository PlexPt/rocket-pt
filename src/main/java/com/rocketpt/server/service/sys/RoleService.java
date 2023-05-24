package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.base.OrderPageParam;
import com.rocketpt.server.common.base.PageUtil;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dao.RoleDao;
import com.rocketpt.server.dto.entity.ResourceEntity;
import com.rocketpt.server.dto.entity.RoleEntity;
import com.rocketpt.server.dto.entity.RoleResourceEntity;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.entity.UserRoleEntity;
import com.rocketpt.server.dto.event.RoleCreated;
import com.rocketpt.server.dto.event.RoleUpdated;
import com.rocketpt.server.dto.param.RoleUserParam;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final RoleResourceService roleResourceService;
    private final UserService userService;

    public List<RoleEntity> findRoles() {


        List<RoleEntity> entities = list();
        for (RoleEntity entity : entities) {
            List<Long> resourceIds = roleResourceService.getResourceIdsByRoleId(entity.getId());
            entity.setResourceIds(resourceIds);
        }

        return entities;
    }

    public RoleEntity findRoleById(Long roleId) {
        RoleEntity roleEntity = getById(roleId);
        if (roleEntity == null) {
            throw new RocketPTException(CommonResultStatus.RECORD_NOT_EXIST);
        }
        return roleEntity;
    }

    public RoleEntity changeMenu(Long roleId, Set<ResourceEntity> menuEntities) {
        roleResourceService.remove(new QueryWrapper<RoleResourceEntity>()
                .lambda()
                .eq(RoleResourceEntity::getRoleId, roleId)
        );
        List<RoleResourceEntity> roleMenuEntities = menuEntities.stream()
                .map(ResourceEntity::getId)
                .map(rid -> new RoleResourceEntity(null, roleId, rid))
                .collect(Collectors.toList());
        roleResourceService.saveBatch(roleMenuEntities);

        RoleEntity roleEntity = findRoleById(roleId);
        roleEntity.setResourceEntities(menuEntities);
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

    public void changeUsers(RoleUserParam param) {
        Long roleId = param.getRoleId();
        userRoleService.remove(new QueryWrapper<UserRoleEntity>()
                .lambda()
                .eq(UserRoleEntity::getRoleId, roleId)
        );

        List<UserRoleEntity> userRoleEntities = param.getUserIds()
                .stream()
                .map(userId -> new UserRoleEntity(null, (long) userId, roleId))
                .collect(Collectors.toList());

        userRoleService.saveBatch(userRoleEntities);

    }

    @Transactional(rollbackFor = Exception.class)
    public RoleEntity updateRole(Long roleId, String name, String description) {
        RoleEntity role = findRoleById(roleId);
        role.setName(name);
        role.setRemark(description);
        updateById(role);
//        DomainEventPublisher.instance().publish(new RoleUpdated(role));
        return role;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleById(Long roleId) {
        removeById(roleId);
        roleResourceService.remove(new QueryWrapper<RoleResourceEntity>()
                .lambda()
                .eq(RoleResourceEntity::getRoleId, roleId)
        );
        userRoleService.remove(new QueryWrapper<UserRoleEntity>()
                .lambda()
                .eq(UserRoleEntity::getRoleId, roleId)
        );

    }

    /**
     * 根据角色获取所有拥有角色的用户
     *
     * @param roleId
     * @param param
     * @return
     */
    public Result findRoleUsers(Long roleId, OrderPageParam param) {
        PageHelper.startPage(param.getPage(), param.getSize());

        List<UserRoleEntity> list = userRoleService.list(Wrappers.<UserRoleEntity>lambdaQuery()
                .eq(UserRoleEntity::getRoleId, roleId)
        );
        List<Long> userids = list.stream()
                .map(UserRoleEntity::getUserId)
                .toList();

        List<UserEntity> users = userService.findUserByIds(userids);

        return Result.ok(users, PageUtil.getPage(list));

    }

    public void createRole(RoleEntity entity) {
        entity.setId(null);
        entity.setCreateBy(userService.getUserId());
        entity.setUpdateBy(userService.getUserId());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        save(entity);
        DomainEventPublisher.instance().publish(new RoleCreated(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeResources(RoleEntity entity) {
        Long roleId = entity.getId();
        entity.setUpdateBy(userService.getUserId());
        entity.setUpdateTime(LocalDateTime.now());

        updateById(entity);

        roleResourceService.remove(new QueryWrapper<RoleResourceEntity>()
                .lambda()
                .eq(RoleResourceEntity::getRoleId, roleId)
        );
        List<RoleResourceEntity> roleMenuEntities = entity.getResourceIds()
                .stream()
                .map(rid -> new RoleResourceEntity(null, roleId, rid))
                .collect(Collectors.toList());

        roleResourceService.saveBatch(roleMenuEntities);
    }
}
