package com.rocketpt.server.sys.event;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.dto.entity.User;

/**
 * @author plexpt
 */
public record UserCreated(User user) implements DomainEvent {
}
