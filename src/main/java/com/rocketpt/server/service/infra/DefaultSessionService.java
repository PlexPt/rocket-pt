package com.rocketpt.server.service.infra;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.SessionItemHolder;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.entity.UserCredential;
import com.rocketpt.server.dto.event.UserLoggedIn;
import com.rocketpt.server.dto.event.UserLoggedOut;
import com.rocketpt.server.dto.sys.UserinfoDTO;
import com.rocketpt.server.dao.UserCredentialDao;
import com.rocketpt.server.dao.UserDao;
import com.rocketpt.server.service.sys.SessionService;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import static com.rocketpt.server.dto.entity.UserCredential.IdentityType.PASSWORD;

/**
 * @author plexpt
 */
@Service
@RequiredArgsConstructor
public class DefaultSessionService implements SessionService {

    private final UserCredentialDao userCredentialDao;

    private final SessionManager sessionManager;

    private final UserDao userDao;


    @Override
    public UserinfoDTO login(String username, String password) {
        UserCredential credential = userCredentialDao.findCredential(username, PASSWORD)
                .orElseThrow(() -> new UserException(CommonResultStatus.UNAUTHORIZED, "密码不正确"));
        if (credential.doCredentialMatch(password)) {
            UserEntity userEntity = userDao.selectById(credential.getUserId());
            if (userEntity.userLocked()) {
                throw new UserException(CommonResultStatus.UNAUTHORIZED, "用户已经禁用，请与管理员联系");
            }
            Set<String> permissions = userDao.listUserPermissions(userEntity.getId());
            String token = UUID.randomUUID().toString().replace("-", "");
            UserinfoDTO userinfo = new UserinfoDTO(token, userEntity.getId(), userEntity.getUsername(),
                    userEntity.getFullName(), userEntity.getAvatar(),
                    new UserinfoDTO.Credential(credential.getIdentifier(),
                            credential.getIdentityType(), credential.getPasskey()), permissions);
            sessionManager.store(token, credential, userinfo);
            SessionItemHolder.setItem(Constants.SESSION_CURRENT_USER, userinfo);
            DomainEventPublisher.instance().publish(new UserLoggedIn(userinfo));
            return userinfo;
        } else {
            throw new UserException(CommonResultStatus.UNAUTHORIZED, "密码不正确");
        }
    }

    @Override
    public void logout(String token) {
        UserinfoDTO userinfo = (UserinfoDTO) sessionManager.get(token);
        sessionManager.invalidate(token);
        DomainEventPublisher.instance().publish(new UserLoggedOut(userinfo));
    }

    @Override
    public boolean isLogin(String token) {
        return sessionManager.get(token) != null;
    }

    @Override
    public UserinfoDTO getLoginUserInfo(String token) {
        return (UserinfoDTO) sessionManager.get(token);
    }

    @Override
    public void refresh() {
        sessionManager.refresh();
    }
}
