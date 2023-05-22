package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dto.entity.RoleMenuEntity;
import com.rocketpt.server.dao.RoleResourceDao;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleMenuService extends ServiceImpl<RoleResourceDao, RoleMenuEntity> {
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        List<RoleMenuEntity> list = list(Wrappers.lambdaQuery(RoleMenuEntity.class)
                .select(RoleMenuEntity::getMenuId)
                .eq(RoleMenuEntity::getRoleId, roleId)
        );
        if (list == null) {
            return new ArrayList<>();
        }

        List<Long> ids = list.stream()
                .map(RoleMenuEntity::getMenuId)
                .toList();

        return ids;
    }

}
