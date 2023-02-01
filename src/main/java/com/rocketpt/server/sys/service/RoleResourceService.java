package com.rocketpt.server.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dto.entity.RoleResource;
import com.rocketpt.server.sys.repository.RoleResourceDao;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleResourceService extends ServiceImpl<RoleResourceDao, RoleResource> {

}
