package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dao.TagDao;
import com.rocketpt.server.dto.entity.TagEntity;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService extends ServiceImpl<TagDao, TagEntity> {


}
