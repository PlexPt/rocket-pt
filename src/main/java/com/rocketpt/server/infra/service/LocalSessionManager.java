package com.rocketpt.server.infra.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rocketpt.server.common.JsonUtils;
import com.rocketpt.server.sys.entity.Session;
import com.rocketpt.server.sys.entity.User;
import com.rocketpt.server.sys.entity.UserCredential;
import com.rocketpt.server.sys.repository.SessionRepository;
import com.rocketpt.server.sys.repository.UserCredentialRepository;
import com.rocketpt.server.sys.repository.UserRepository;
import com.rocketpt.server.sys.dto.UserinfoDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author plexpt
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LocalSessionManager implements SessionManager {

    private static final long EXPIRE_DAYS = 15;

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    private final Cache<String, Session> cache = Caffeine.newBuilder()
            .initialCapacity(100)
            .expireAfterAccess(EXPIRE_DAYS, TimeUnit.DAYS)
            .build();

    private final ScheduledExecutorService sessionCheckExecutor =
            new ScheduledThreadPoolExecutor(1);

    @PostConstruct
    private void checkSession() {
        log.info("checkSession....");
        sessionCheckExecutor.scheduleWithFixedDelay(() -> {
            try {
                ConcurrentMap<String, Session> map = cache.asMap();
                sessionRepository.deleteExpiredSession();
                map.forEach((key, value) -> {
                    if (value.isActive()) {
                        value.setActive(false);
                        sessionRepository.updateExpireTime(key, value.getExpireTime());
                    }
                });
            } catch (Exception e) {
                log.error("{}", e.getMessage(), e);
            }
        }, 5, 30, TimeUnit.SECONDS);
    }

    @Override
    public void store(String key, UserCredential credential, Serializable value) {
        Session currentSession = cache.getIfPresent(key);
        Session newSession = Session.of(currentSession != null ? currentSession.getId() : null,
                key, credential, value, getLatestExpireTime());
        sessionRepository.insert(newSession);
        cache.put(key, newSession);
    }

    private static LocalDateTime getLatestExpireTime() {
        return LocalDateTime.now().plus(EXPIRE_DAYS, ChronoUnit.DAYS);
    }


    @Override
    public void invalidate(String key) {
        Session session = cache.getIfPresent(key);
        cache.invalidate(key);
        assert session != null;
        sessionRepository.deleteById(session);
    }

    @Override
    public Object get(String key) {
        try {
            Session session;
            if ((session = cache.getIfPresent(key)) == null &&
                    (session = sessionRepository.findByToken(key)) == null) {
                return null;
            }
            session.setExpireTime(getLatestExpireTime());
            session.setActive(true);
            cache.put(key, session);
            return JsonUtils.parseToObject(session.getData(), UserinfoDTO.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh() {
        List<Session> sessions = sessionRepository.selectList(new QueryWrapper<>());

        sessions.forEach(session -> {
            UserCredential credential =
                    userCredentialRepository.selectById(session.getCredentialId());
            User user = userRepository.selectById(credential.getUserId());
            Set<String> permissions = userRepository.listUserPermissions(user.getId());

            UserinfoDTO userinfo = new UserinfoDTO(session.getToken(), user.getId(),
                    user.getUsername(), user.getFullName(), user.getAvatar(),
                    new UserinfoDTO.Credential(credential.getIdentifier(),
                            credential.getIdentityType()), permissions);
            session.setData(JsonUtils.stringify(userinfo));
            sessionRepository.updateById(session);
        });
        cache.invalidateAll();
    }
}
