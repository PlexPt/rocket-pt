package com.rocketpt.server.dto.event;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.dto.entity.MenuEntity;

/**
 * @author plexpt
 */
public record ResourceDeleted(MenuEntity menuEntity) implements DomainEvent {
}
