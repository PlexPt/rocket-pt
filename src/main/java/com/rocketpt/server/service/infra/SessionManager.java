package com.rocketpt.server.service.infra;

import com.rocketpt.server.dto.entity.UserCredentialEntity;

import java.io.Serializable;

/**
 * @author plexpt
 */
public interface SessionManager {

    void store(String key, UserCredentialEntity credential, Serializable value);

    void invalidate(String key);

    Object get(String key);

    void refresh();
}
