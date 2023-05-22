package com.rocketpt.server.dto.sys;

import com.rocketpt.server.dto.entity.UserCredential;

import java.io.Serializable;
import java.util.Set;

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
