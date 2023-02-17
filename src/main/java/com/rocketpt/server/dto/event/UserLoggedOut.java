package com.rocketpt.server.dto.event;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.dto.sys.UserinfoDTO;

/**
 * @author plexpt
 */
public record UserLoggedOut(UserinfoDTO dto) implements DomainEvent {
}
