package com.rocketpt.server.dto.sys;

import java.util.List;

/**
 * @author plexpt
 */
public record RoleDTO(Long id, String name, String description, boolean available,
                      List<Long> resourceIds) {
}
