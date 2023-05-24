package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dto.entity.RoleResourceEntity;
import com.rocketpt.server.dao.RoleResourceDao;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleResourceService extends ServiceImpl<RoleResourceDao, RoleResourceEntity> {
    public List<Integer> getResourceIdsByRoleId(Integer roleId) {
        List<RoleResourceEntity> list = list(Wrappers.lambdaQuery(RoleResourceEntity.class)
                .select(RoleResourceEntity::getResourceId)
                .eq(RoleResourceEntity::getRoleId, roleId)
        );
        if (list == null) {
            return new ArrayList<>();
        }

        List<Integer> ids = list.stream()
                .map(RoleResourceEntity::getResourceId)
                .toList();

        return ids;
    }

}
