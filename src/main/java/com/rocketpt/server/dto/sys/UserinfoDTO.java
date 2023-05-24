package com.rocketpt.server.dto.sys;

import java.io.Serializable;
import java.util.Set;

/**
 * @author plexpt
 */
public record UserinfoDTO(String token,
                          Integer userId,
                          String username,
                          String fullName,
                          String avatar,
                          Credential credential,
                          Set<String> permissions) implements Serializable {

    public record Credential(String identifier, String passkey) {
    }

}
