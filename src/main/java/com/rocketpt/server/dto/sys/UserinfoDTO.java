package com.rocketpt.server.dto.sys;

import java.io.Serializable;
import java.util.Set;

import com.rocketpt.server.dto.entity.UserCredential;

/**
 * @author plexpt
 */
public record UserinfoDTO(String token,
                          Long userId,
                          String username,
                          String fullName,
                          String avatar,
                          Credential credential,
                          Set<String> permissions) implements Serializable {

    public record Credential(String identifier, UserCredential.IdentityType type, String passkey) {
    }

}
