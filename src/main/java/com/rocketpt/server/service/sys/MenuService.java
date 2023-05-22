package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dto.entity.MenuEntity;
import com.rocketpt.server.dto.event.ResourceCreated;
import com.rocketpt.server.dto.event.ResourceDeleted;
import com.rocketpt.server.dto.event.ResourceUpdated;
import com.rocketpt.server.dto.sys.MenuResourceDTO;
import com.rocketpt.server.dto.sys.ResourceTreeDTO;
import com.rocketpt.server.dao.MenuDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class MenuService extends ServiceImpl<MenuDao, MenuEntity> {

    public Set<MenuEntity> findMenuByIds(Set<Long> ids) {
        List<MenuEntity> list = listByIds(ids);
        return new LinkedHashSet<>(list);
    }

    public MenuEntity findResourceById(Long resourceId) {
        MenuEntity menuEntity = getById(resourceId);
        if (menuEntity == null) {
            throw new UserException(CommonResultStatus.RECORD_NOT_EXIST);
        }
        return menuEntity;
    }

    public List<MenuResourceDTO> findMenus() {

        List<MenuEntity> allMenus = list(new QueryWrapper<MenuEntity>()
                .lambda()
                .eq(MenuEntity::getType, MenuEntity.Type.MENU)
                .orderByAsc(MenuEntity::getOrderNum)
        );
        List<MenuResourceDTO> list = new ArrayList<>();
        for (MenuEntity menu : allMenus) {

            if (!StpUtil.hasPermission(menu.getPermission())) {
                continue;
            }

            list.add(new MenuResourceDTO(menu.getId(), menu.getName(), menu.getUrl(),
                    menu.getIcon(), menu.getParentId()));
        }
        return list;
    }


    public List<ResourceTreeDTO> findResourceTree() {
        List<MenuEntity> allMenuEntities = list();
        return getResourceTree(allMenuEntities, Constants.RESOURCE_ROOT_ID);
    }

    private List<ResourceTreeDTO> getResourceTree(List<MenuEntity> menuEntities, Long parentId) {
        return menuEntities.stream()
                .filter(r -> r.getParentId() != null && r.getParentId().equals(parentId))
                .map(r -> new ResourceTreeDTO(r.getId(), r.getName(), r.getType(),
                        r.getPermission(), r.getUrl(), r.getIcon(), getResourceTree(menuEntities,
                        r.getId()), r.getParentId(), getParentName(r.getParentId(), menuEntities)))
                .collect(Collectors.toList());
    }

    private String getParentName(Long parentId, List<MenuEntity> menuEntities) {
        for (MenuEntity menuEntity : menuEntities) {
            if (menuEntity.getId().equals(parentId)) {
                return menuEntity.getName();
            }
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public MenuEntity createResource(String name, MenuEntity.Type type, String url, String icon,
                                     String permission, Long parentId) {
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setName(name);
        menuEntity.setType(type);
        menuEntity.setUrl(url);
        menuEntity.setIcon(icon);
        menuEntity.setPermission(permission);
        menuEntity.setParentId((parentId));
        save(menuEntity);
        DomainEventPublisher.instance().publish(new ResourceCreated(menuEntity));
        return menuEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public MenuEntity updateResource(Long resourceId, String name, MenuEntity.Type type, String url,
                                     String icon, String permission, Long parentId) {
        MenuEntity menuEntity = findResourceById(resourceId);
        menuEntity.setName(name);
        menuEntity.setType(type);
        menuEntity.setUrl(url);
        menuEntity.setIcon(icon);
        menuEntity.setPermission(permission);
        menuEntity.setParent(findResourceById(parentId));
        updateById(menuEntity);
        DomainEventPublisher.instance().publish(new ResourceUpdated(menuEntity));
        return menuEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteResourceById(Long resourceId) {
        MenuEntity menuEntity = findResourceById(resourceId);
        removeById(resourceId);
        DomainEventPublisher.instance().publish(new ResourceDeleted(menuEntity));
    }


}
