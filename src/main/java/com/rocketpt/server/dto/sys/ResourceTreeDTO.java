package com.rocketpt.server.dto.sys;

import com.rocketpt.server.dto.entity.ResourceEntity.Type;

import java.util.List;

/**
 * @author plexpt
 */
public record ResourceTreeDTO(Long id, String name, Type type, String permission, String url,
                              String icon,
                              List<ResourceTreeDTO> children, Long parentId, String parentName) {

}
