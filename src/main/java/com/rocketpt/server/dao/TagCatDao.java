package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.TagCatEntity;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TagCatDao extends BaseMapper<TagCatEntity> {
}
