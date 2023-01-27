package com.rocketpt.server.infra.service;

import com.rocketpt.server.sys.entity.UserCredential;

import java.io.Serializable;

/**
 * @author plexpt
 */
public interface SessionManager {

    void store(String key, UserCredential credential, Serializable value);

    void invalidate(String key);

    Object get(String key);

    void refresh();
}
