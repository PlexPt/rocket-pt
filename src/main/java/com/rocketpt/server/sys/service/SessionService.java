package com.rocketpt.server.sys.service;

import org.springframework.stereotype.Service;

import com.rocketpt.server.dto.sys.UserinfoDTO;

/**
 * @author plexpt
 */
@Service
public interface SessionService {

    UserinfoDTO login(String username, String password);

    void logout(String token);

    boolean isLogin(String token);

    UserinfoDTO getLoginUserInfo(String token);

    void refresh();

}
