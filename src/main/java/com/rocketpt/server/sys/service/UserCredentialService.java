package com.rocketpt.server.sys.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.entity.UserCredential;
import com.rocketpt.server.infra.service.PasskeyManager;
import com.rocketpt.server.sys.repository.UserCredentialRepository;
import com.rocketpt.server.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCredentialService extends ServiceImpl<UserCredentialRepository, UserCredential> {

    private final PasskeyManager passkeyManager;

    @SneakyThrows
    String generate(String username, String password) {
        return SecurityUtil.md5(username, password);
    }

    void refreshPasskey(long userId) {
        String key = passkeyManager.generate(userId);
        Optional<UserCredential> optional = Optional.ofNullable(
                getOne(
                        Wrappers.<UserCredential>lambdaQuery()
                                .eq(UserCredential::getUserId, userId)
                )
        );
        if (optional.isEmpty()) throw new RocketPTException(CommonResultStatus.PARAM_ERROR);
        UserCredential credential = optional.get();
        credential.setPasskey(key);
        updateById(credential);
    }
}
