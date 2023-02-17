package com.rocketpt.server.infra.service;

/**
 * @author yzr
 */
public interface CheckCodeManager {

    String generate(long userId, String email);

}
