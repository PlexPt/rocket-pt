package com.rocketpt.server.service.infra;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rocketpt.server.dto.entity.SessionEntity;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.entity.UserCredential;
import com.rocketpt.server.dto.sys.UserinfoDTO;
import com.rocketpt.server.dao.SessionDao;
import com.rocketpt.server.dao.UserCredentialDao;
import com.rocketpt.server.dao.UserDao;
import com.rocketpt.server.util.JsonUtils;

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

    private final SessionDao sessionDao;
    private final UserDao userDao;
    private final UserCredentialDao userCredentialDao;

    private final Cache<String, SessionEntity> cache = Caffeine.newBuilder()
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
                ConcurrentMap<String, SessionEntity> map = cache.asMap();
                sessionDao.deleteExpiredSession();
                map.forEach((key, value) -> {
                    if (value.isActive()) {
                        value.setActive(false);
                        sessionDao.updateExpireTime(key, value.getExpireTime());
                    }
                });
            } catch (Exception e) {
                log.error("{}", e.getMessage(), e);
            }
        }, 5, 60, TimeUnit.SECONDS);
    }

    @Override
    public void store(String key, UserCredential credential, Serializable value) {
        SessionEntity currentSessionEntity = cache.getIfPresent(key);
        SessionEntity newSessionEntity = SessionEntity.of(currentSessionEntity != null ? currentSessionEntity.getId() : null,
                key, credential, value, getLatestExpireTime());
        sessionDao.insert(newSessionEntity);
        cache.put(key, newSessionEntity);
    }

    private static LocalDateTime getLatestExpireTime() {
        return LocalDateTime.now().plus(EXPIRE_DAYS, ChronoUnit.DAYS);
    }


    @Override
    public void invalidate(String key) {
        SessionEntity sessionEntity = cache.getIfPresent(key);
        cache.invalidate(key);
        assert sessionEntity != null;
        sessionDao.deleteById(sessionEntity);
    }

    @Override
    public Object get(String key) {
        try {
            SessionEntity sessionEntity;
            if ((sessionEntity = cache.getIfPresent(key)) == null &&
                    (sessionEntity = sessionDao.findByToken(key)) == null) {
                return null;
            }
            sessionEntity.setExpireTime(getLatestExpireTime());
            sessionEntity.setActive(true);
            cache.put(key, sessionEntity);
            return JsonUtils.parseToObject(sessionEntity.getData(), UserinfoDTO.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh() {
        List<SessionEntity> sessionEntities = sessionDao.selectList(new QueryWrapper<>());

        sessionEntities.forEach(sessionEntity -> {
            UserCredential credential =
                    userCredentialDao.selectById(sessionEntity.getCredentialId());
            UserEntity userEntity = userDao.selectById(credential.getUserId());
            Set<String> permissions = userDao.listUserPermissions(userEntity.getId());

            UserinfoDTO userinfo = new UserinfoDTO(sessionEntity.getToken(), userEntity.getId(),
                    userEntity.getUsername(), userEntity.getFullName(), userEntity.getAvatar(),
                    new UserinfoDTO.Credential(credential.getIdentifier(),
                            credential.getIdentityType(), credential.getPasskey()), permissions);
            sessionEntity.setData(JsonUtils.stringify(userinfo));
            sessionDao.updateById(sessionEntity);
        });
        cache.invalidateAll();
    }
}
