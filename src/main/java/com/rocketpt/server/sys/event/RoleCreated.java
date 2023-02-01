package com.rocketpt.server.sys.event;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.dto.entity.Role;

/**
 * @author plexpt
 */
public record RoleCreated(Role role) implements DomainEvent {
}
