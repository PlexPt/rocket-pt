package com.rocketpt.server.sys.event;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.sys.entity.Resource;

/**
 * @author plexpt
 */
public record ResourceUpdated(Resource resource) implements DomainEvent {
}
