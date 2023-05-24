package com.rocketpt.server.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rocketpt.server.dao.UserDao;
import com.rocketpt.server.dto.entity.RoleEntity;
import com.rocketpt.server.dto.entity.UserRoleEntity;
import com.rocketpt.server.service.sys.ResourceService;
import com.rocketpt.server.service.sys.RoleResourceService;
import com.rocketpt.server.service.sys.RoleService;
import com.rocketpt.server.service.sys.UserRoleService;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.dev33.satoken.stp.StpInterface;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SaTokenInterfaceConfigure implements StpInterface {

    final ResourceService resourceService;
    final RoleService roleService;
    final UserRoleService userRoleService;
    final RoleResourceService roleResourceService;
    final UserDao userDao;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        Integer id = (Integer.parseInt((String) loginId));

        List<String> list = userDao.queryAllPerms(id);

        if (!CollectionUtils.isEmpty(list)) {
            list = list.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
        }


        return list;

    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roles = new ArrayList<>();

        List<UserRoleEntity> list = userRoleService.list(new QueryWrapper<UserRoleEntity>()
                .lambda()
                .eq(UserRoleEntity::getUserId, loginId)
        );

        if (!CollectionUtils.isEmpty(list)) {
            List<Integer> ids = list.stream()
                    .map(UserRoleEntity::getRoleId)
                    .toList();
            List<RoleEntity> roleEntities = roleService.list(new QueryWrapper<RoleEntity>()
                    .lambda()
                    .in(RoleEntity::getId, ids));

            roles = roleEntities.stream()
                    .map(RoleEntity::getName)
                    .toList();
        }

        return roles;
    }
}
