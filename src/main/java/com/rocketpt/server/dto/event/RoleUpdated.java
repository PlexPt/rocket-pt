package com.rocketpt.server.dto.event;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.dto.entity.Role;

/**
 * @author plexpt
 */
public record RoleUpdated(Role role) implements DomainEvent {
}
