package com.rocketpt.server.dto.event;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.dto.entity.ResourceEntity;

/**
 * @author plexpt
 */
public record ResourceDeleted(ResourceEntity resourceEntity) implements DomainEvent {
}
