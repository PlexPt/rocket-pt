package com.rocketpt.server.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dto.entity.UserCredential;
import com.rocketpt.server.sys.repository.UserCredentialRepository;
import com.rocketpt.server.util.SecurityUtil;

import org.springframework.stereotype.Service;

import cn.hutool.core.lang.UUID;

/**
 * @author pt
 */
@Service
public class UserCredentialService extends ServiceImpl<UserCredentialRepository, UserCredential> {


    public void register(String userName, Long userId, String password) {


        UserCredential userCredential = new UserCredential();
        userCredential.setUserId(userId);
        userCredential.setIdentifier(userName);
        userCredential.setIdentityType(UserCredential.IdentityType.PASSWORD);
        userCredential.setCredential(SecurityUtil.md5(userName, password));
        userCredential.setPasskey(UUID.fastUUID().toString(true));

        save(userCredential);

    }


}
