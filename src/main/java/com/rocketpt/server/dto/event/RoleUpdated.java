package com.rocketpt.server.dto.event;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.dto.entity.RoleEntity;

/**
 * @author plexpt
 */
public record RoleUpdated(RoleEntity roleEntity) implements DomainEvent {
}
