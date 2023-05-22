package com.rocketpt.server.service.infra;

/**
 * @author yzr
 */
public interface PasskeyManager {

    String generate(long userId);

}
