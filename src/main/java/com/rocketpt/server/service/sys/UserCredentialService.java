package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.entity.UserCredential;
import com.rocketpt.server.service.infra.PasskeyManager;
import com.rocketpt.server.dao.UserCredentialDao;
import com.rocketpt.server.util.SecurityUtil;

import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class UserCredentialService extends ServiceImpl<UserCredentialDao, UserCredential> {

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
        if (optional.isEmpty()) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR);
        }
        UserCredential credential = optional.get();
        credential.setPasskey(key);
        updateById(credential);
    }
}
