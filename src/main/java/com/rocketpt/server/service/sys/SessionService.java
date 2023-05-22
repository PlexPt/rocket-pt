package com.rocketpt.server.service.sys;

import com.rocketpt.server.dto.sys.UserinfoDTO;

import org.springframework.stereotype.Service;

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
