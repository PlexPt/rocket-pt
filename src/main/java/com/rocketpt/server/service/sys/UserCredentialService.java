package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dao.UserCredentialDao;
import com.rocketpt.server.dto.entity.UserCredentialEntity;
import com.rocketpt.server.service.infra.PasskeyManager;

import org.springframework.stereotype.Service;

import java.util.Optional;

import cn.dev33.satoken.secure.SaSecureUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCredentialService extends ServiceImpl<UserCredentialDao, UserCredentialEntity> {

    private final PasskeyManager passkeyManager;

    /**
     * 生成加密的密码
     *
     * @param password
     * @param salt
     * @return
     */
    public String generate(String password, String salt) {
        return SaSecureUtil.sha256(password + salt);
    }

    /**
     * 更新密码
     */
    public void updatePassword(Integer userId, String newPassword) {
        String salt = getById(userId).getSalt();
        updatePassword(userId, newPassword, salt);
    }

    /**
     * 更新totp
     */
    public void updateTotp(Integer userId, String newTotp) {
        update(new LambdaUpdateWrapper<UserCredentialEntity>()
                .eq(UserCredentialEntity::getId, userId)
                .set(UserCredentialEntity::getTotp, newTotp)
        );
    }

    /**
     * 去掉remove
     */
    public void removeTotp(Integer userId) {
        update(new LambdaUpdateWrapper<UserCredentialEntity>()
                .eq(UserCredentialEntity::getId, userId)
                .set(UserCredentialEntity::getTotp, null)
        );
    }


    /**
     * 更新密码
     */
    public void updatePassword(Integer userId, String newPassword, String salt) {
        String generatedPassword = generate(newPassword, salt);

        update(new LambdaUpdateWrapper<UserCredentialEntity>()
                .eq(UserCredentialEntity::getId, userId)
                .set(UserCredentialEntity::getPassword, generatedPassword)
        );

    }

    /**
     * 根据用户名获取
     *
     * @param username
     * @return
     */
    public UserCredentialEntity getByUsername(String username) {
        return getOne(new QueryWrapper<UserCredentialEntity>()
                .lambda()
                .eq(UserCredentialEntity::getUsername, username), false
        );
    }

    /**
     * 获取
     *
     * @return
     */
    public UserCredentialEntity getByCheckCode(String code) {
        return getOne(new QueryWrapper<UserCredentialEntity>()
                .lambda()
                .eq(UserCredentialEntity::getCheckCode, code), false
        );
    }


    /**
     * 重置用户PASSKEY
     *
     * @param userId
     */
    public void refreshPasskey(Integer userId) {

        UserCredentialEntity credentialEntity = getOne(
                Wrappers.<UserCredentialEntity>lambdaQuery()
                        .eq(UserCredentialEntity::getId, userId)
        );

        UserCredentialEntity credential = Optional.ofNullable(credentialEntity)
                .orElseThrow(() -> new RocketPTException(CommonResultStatus.PARAM_ERROR));

        String key = passkeyManager.generate(userId);

        credential.setPasskey(key);
        updateById(credential);
    }

    public String resetCheckCode(Integer userId) {
        String checkCode = passkeyManager.generate(userId);

        update(new LambdaUpdateWrapper<UserCredentialEntity>()
                .eq(UserCredentialEntity::getId, userId)
                .set(UserCredentialEntity::getCheckCode, checkCode)
        );

        return checkCode;
    }
}
