package com.rocketpt.server.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dto.entity.UserRole;
import com.rocketpt.server.sys.repository.UserRoleDao;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleService extends ServiceImpl<UserRoleDao, UserRole> {

}
