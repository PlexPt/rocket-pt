package com.rocketpt.server.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.authz.PermissionHelper;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dto.entity.Resource;
import com.rocketpt.server.dto.entity.Resource.Type;
import com.rocketpt.server.sys.event.ResourceCreated;
import com.rocketpt.server.sys.event.ResourceDeleted;
import com.rocketpt.server.sys.event.ResourceUpdated;
import com.rocketpt.server.sys.repository.ResourceRepository;
import com.rocketpt.server.sys.dto.MenuResourceDTO;
import com.rocketpt.server.sys.dto.ResourceTreeDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import static com.rocketpt.server.common.Constants.RESOURCE_ROOT_ID;

/**
 * @author plexpt
 */
@Service
@RequiredArgsConstructor
public class ResourceService extends ServiceImpl<ResourceRepository, Resource> {

    public Set<Resource> findResourceByIds(Set<Long> resourceIds) {
        List<Resource> list = listByIds(resourceIds);
        return new LinkedHashSet<>(list);
    }

    public Resource findResourceById(Long resourceId) {
        Resource resource = getById(resourceId);
        if (resource == null) {
            throw new UserException(CommonResultStatus.RECORD_NOT_EXIST);
        }
        return resource;
    }

    public List<MenuResourceDTO> findMenus(Set<String> permissions) {

        List<Resource> allMenus = list(new QueryWrapper<Resource>()
                .lambda()
                .eq(Resource::getType, Type.MENU)
        );
        List<MenuResourceDTO> list = new ArrayList<>();
        for (Resource menu : allMenus) {
            if (!PermissionHelper.hasPermission(permissions, menu.getPermission())) {
                continue;
            }

            list.add(new MenuResourceDTO(menu.getId(), menu.getName(), menu.getUrl(),
                    menu.getIcon(), menu.getParentId()));
        }
        return list;
    }


    public List<ResourceTreeDTO> findResourceTree() {
        List<Resource> allResources = list();
        return getResourceTree(allResources, RESOURCE_ROOT_ID);
    }

    private List<ResourceTreeDTO> getResourceTree(List<Resource> resources, Long parentId) {
        return resources.stream()
                .filter(r -> r.getParentId() != null && r.getParentId().equals(parentId))
                .map(r -> new ResourceTreeDTO(r.getId(), r.getName(), r.getType(),
                        r.getPermission(), r.getUrl(), r.getIcon(), getResourceTree(resources,
                        r.getId()), r.getParentId(), getParentName(r.getParentId(), resources)))
                .collect(Collectors.toList());
    }

    private String getParentName(Long parentId, List<Resource> resources) {
        for (Resource resource : resources) {
            if (resource.getId().equals(parentId)) {
                return resource.getName();
            }
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Resource createResource(String name, Type type, String url, String icon,
                                   String permission, Long parentId) {
        Resource resource = new Resource();
        resource.setName(name);
        resource.setType(type);
        resource.setUrl(url);
        resource.setIcon(icon);
        resource.setPermission(permission);
        resource.setParentId( (parentId));
        save(resource);
        DomainEventPublisher.instance().publish(new ResourceCreated(resource));
        return resource;
    }

    @Transactional(rollbackFor = Exception.class)
    public Resource updateResource(Long resourceId, String name, Type type, String url,
                                   String icon, String permission, Long parentId) {
        Resource resource = findResourceById(resourceId);
        resource.setName(name);
        resource.setType(type);
        resource.setUrl(url);
        resource.setIcon(icon);
        resource.setPermission(permission);
        resource.setParent(findResourceById(parentId));
        updateById(resource);
        DomainEventPublisher.instance().publish(new ResourceUpdated(resource));
        return resource;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteResourceById(Long resourceId) {
        Resource resource = findResourceById(resourceId);
        removeById(resourceId);
        DomainEventPublisher.instance().publish(new ResourceDeleted(resource));
    }


}
