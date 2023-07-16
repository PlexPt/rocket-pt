package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dao.UserRoleDao;
import com.rocketpt.server.dto.entity.UserRoleEntity;
import com.rocketpt.server.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleService extends ServiceImpl<UserRoleDao, UserRoleEntity> {

    final SysConfigService sysConfigService;

    public void register(Integer userId) {
        //todo 配置默认角色ID
        int roleId = 3;
        UserRoleEntity entity = new UserRoleEntity();
        entity.setUserId(userId);
        entity.setRoleId(roleId);
        save(entity);
    }
}
