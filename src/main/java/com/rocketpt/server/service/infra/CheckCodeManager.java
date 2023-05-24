package com.rocketpt.server.service.infra;

/**
 * @author yzr
 */
public interface CheckCodeManager {

    String generate(Integer userId, String email);

}
