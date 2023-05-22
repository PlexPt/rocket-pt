package com.rocketpt.server.service.infra;

/**
 * @author yzr
 */
public interface CheckCodeManager {

    String generate(long userId, String email);

}
