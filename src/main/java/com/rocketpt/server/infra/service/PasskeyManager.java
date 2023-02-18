package com.rocketpt.server.infra.service;

import com.rocketpt.server.dto.entity.UserCredential;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
 * @author yzr
 */
public interface PasskeyManager {

    String generate(long userId);

}
