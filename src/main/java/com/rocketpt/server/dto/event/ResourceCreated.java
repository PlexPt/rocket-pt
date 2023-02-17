package com.rocketpt.server.dto.event;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.dto.entity.Resource;

/**
 * @author plexpt
 */
public record ResourceCreated(Resource resource) implements DomainEvent {
}
