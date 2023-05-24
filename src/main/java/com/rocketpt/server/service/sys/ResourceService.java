package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dao.ResourceDao;
import com.rocketpt.server.dto.entity.ResourceEntity;
import com.rocketpt.server.dto.event.ResourceCreated;
import com.rocketpt.server.dto.event.ResourceDeleted;
import com.rocketpt.server.dto.event.ResourceUpdated;
import com.rocketpt.server.dto.sys.MenuResourceDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@Service
@RequiredArgsConstructor
public class ResourceService extends ServiceImpl<ResourceDao, ResourceEntity> {

    final UserService userService;

    public Set<ResourceEntity> findMenuByIds(Set<Long> ids) {
        List<ResourceEntity> list = listByIds(ids);
        return new LinkedHashSet<>(list);
    }

    public ResourceEntity findResourceById(Long resourceId) {
        ResourceEntity resourceEntity = getById(resourceId);
        if (resourceEntity == null) {
            throw new UserException(CommonResultStatus.RECORD_NOT_EXIST);
        }
        return resourceEntity;
    }

    public List<MenuResourceDTO> listResources() {

        List<ResourceEntity> allMenus = list(new QueryWrapper<ResourceEntity>()
                .lambda()
                .eq(ResourceEntity::getType, ResourceEntity.Type.MENU)
                .orderByAsc(ResourceEntity::getSort)
        );
        List<MenuResourceDTO> list = new ArrayList<>();
        for (ResourceEntity menu : allMenus) {

            if (!StpUtil.hasPermission(menu.getPermission())) {
                continue;
            }

            list.add(new MenuResourceDTO(menu.getId(), menu.getName(), menu.getUrl(),
                    menu.getIcon(), menu.getPid()));
        }
        return list;
    }


    public List<ResourceEntity> findResourceTree() {
        List<ResourceEntity> allResources = list();
        return getResourceTree(allResources, Constants.RESOURCE_ROOT_ID);
    }

    private List<ResourceEntity> getResourceTree(List<ResourceEntity> menuEntities, Integer parentId) {
        return menuEntities.stream()
                .filter(r -> r.getPid() != null && r.getPid().equals(parentId))
                .peek(entity -> {
                    List<ResourceEntity> c = getResourceTree(menuEntities, entity.getId());
                    entity.setChildren(c);
                    entity.setParentName(getParentName(entity.getPid(), menuEntities));
                })
                .collect(Collectors.toList());
    }

    private String getParentName(Integer parentId, List<ResourceEntity> menuEntities) {
        for (ResourceEntity resourceEntity : menuEntities) {
            if (resourceEntity.getId().equals(parentId)) {
                return resourceEntity.getName();
            }
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResourceEntity createResource(String name, ResourceEntity.Type type, String url,
                                         String icon,
                                         String permission, Integer parentId) {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setName(name);
        resourceEntity.setType(type);
        resourceEntity.setUrl(url);
        resourceEntity.setIcon(icon);
        resourceEntity.setPermission(permission);
        resourceEntity.setPid((parentId));
        save(resourceEntity);
        DomainEventPublisher.instance().publish(new ResourceCreated(resourceEntity));
        return resourceEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResourceEntity updateResource(Long resourceId, String name, ResourceEntity.Type type,
                                         String url,
                                         String icon, String permission, Long parentId) {
        ResourceEntity resourceEntity = findResourceById(resourceId);
        resourceEntity.setName(name);
        resourceEntity.setType(type);
        resourceEntity.setUrl(url);
        resourceEntity.setIcon(icon);
        resourceEntity.setPermission(permission);
        resourceEntity.setParent(findResourceById(parentId));
        updateById(resourceEntity);
        DomainEventPublisher.instance().publish(new ResourceUpdated(resourceEntity));
        return resourceEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteResourceById(Long resourceId) {
        ResourceEntity resourceEntity = findResourceById(resourceId);
        removeById(resourceId);
        DomainEventPublisher.instance().publish(new ResourceDeleted(resourceEntity));
    }


    public void createResource(ResourceEntity entity) {
        entity.setId(null);
        entity.setCreateBy(userService.getUserId());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy(userService.getUserId());
        entity.setUpdateTime(LocalDateTime.now());
        save(entity);
        DomainEventPublisher.instance().publish(new ResourceCreated(entity));
    }
}
