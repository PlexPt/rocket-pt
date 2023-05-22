package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dto.entity.UserRoleEntity;
import com.rocketpt.server.dao.UserRoleDao;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleService extends ServiceImpl<UserRoleDao, UserRoleEntity> {

}
