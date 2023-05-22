package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.entity.OrganizationEntity;
import com.rocketpt.server.dto.entity.UserCredential;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.event.UserCreated;
import com.rocketpt.server.dto.event.UserDeleted;
import com.rocketpt.server.dto.event.UserUpdated;
import com.rocketpt.server.dto.param.RegisterParam;
import com.rocketpt.server.dto.sys.PageDTO;
import com.rocketpt.server.service.infra.CheckCodeManager;
import com.rocketpt.server.service.infra.PasskeyManager;
import com.rocketpt.server.dao.UserDao;
import com.rocketpt.server.util.IPUtils;
import com.rocketpt.server.util.JsonUtils;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;

import static com.rocketpt.server.common.CommonResultStatus.RECORD_NOT_EXIST;

/**
 * @author plexpt
 */
@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserDao, UserEntity> {

    private final UserDao userDao;
    private final InvitationService invitationService;
    private final UserCredentialService userCredentialService;
    private final CheckCodeManager checkCodeManager;
    private final PasskeyManager passkeyManager;
    private final CaptchaService captchaService;

    @Transactional(rollbackFor = Exception.class)
    public UserEntity createUser(String username, String fullName, String avatar, UserEntity.Gender gender,
                                 String email, UserEntity.State state, Long organization) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setFullName(fullName);
        userEntity.setAvatar(avatar);
        userEntity.setGender(gender.getCode());
        userEntity.setEmail(email);
        userEntity.setState(state.getCode());
        userEntity.setCreatedTime(LocalDateTime.now());
        userEntity.setOrganizationId(organization);
        save(userEntity);
        DomainEventPublisher.instance().publish(new UserCreated(userEntity));
        return userEntity;
    }

    public Set<UserEntity> findUserByIds(Set<Long> userIds) {
        List<UserEntity> userEntities = listByIds(userIds);
        return new LinkedHashSet<>(userEntities);
    }

    public List<UserEntity> findUserByIds(List<Long> ids) {
        List<UserEntity> list = listByIds(ids);
        return list;
    }

    public UserEntity findUserById(Long userId) {
        UserEntity userEntity = getById(userId);
        if (userEntity == null) {
            throw new RocketPTException(RECORD_NOT_EXIST);
        }
        return userEntity;
    }

    public PageDTO<UserEntity> findOrgUsers(Pageable pageable, String username, UserEntity.State state,
                                                                           OrganizationEntity organizationEntity) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<UserEntity> userEntities = userDao.findOrgUsers(pageable, username, state,
                organizationEntity.getId(),
                organizationEntity.makeSelfAsParentIds());
        long total = new PageInfo(userEntities).getTotal();
        return new PageDTO<>(userEntities, total);
    }

    public boolean existsUsers(OrganizationEntity organizationEntity) {
        String orgParentIds = organizationEntity.makeSelfAsParentIds();
        return userDao.countOrgUsers(organizationEntity.getId(), orgParentIds) > 0;
    }


    @Transactional(rollbackFor = Exception.class)
    public UserEntity updateUser(Long userId, String fullName, String avatar, UserEntity.Gender gender,
                                                                UserEntity.State state, Long organization) {
        UserEntity userEntity = findUserById(userId);
        userEntity.setFullName(fullName);
        userEntity.setAvatar(avatar);
        userEntity.setGender(gender.getCode());
        userEntity.setState(state.getCode());
        userEntity.setOrganizationId(organization);
        updateById(userEntity);
        DomainEventPublisher.instance().publish(new UserUpdated(userEntity));
        return userEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserEntity lockUser(Long userId) {
        UserEntity userEntity = findUserById(userId);
        userEntity.setState(UserEntity.State.LOCKED.getCode());
        updateById(userEntity);
        return userEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserEntity unlockUser(Long userId) {
        UserEntity userEntity = findUserById(userId);
        userEntity.setState(UserEntity.State.NORMAL.getCode());
        updateById(userEntity);
        return userEntity;
    }

    public PageDTO<UserEntity> findUsers(Pageable pageable, UserEntity userEntity) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        Map<String, Object> map = JsonUtils.parseToMap(JsonUtils.stringify(userEntity));
        List<UserEntity> userEntities = listByMap(map);
        long total = new PageInfo(userEntities).getTotal();
        return new PageDTO<>(userEntities, total);
    }

    public boolean isExists(String email) {
        return count(Wrappers.<UserEntity>lambdaQuery()
                .eq(UserEntity::getEmail, email)
        ) != 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId) {
        UserEntity userEntity = findUserById(userId);
        removeById(userEntity);
        DomainEventPublisher.instance().publish(new UserDeleted(userEntity));
    }

    @Transactional(rollbackFor = SQLException.class)
    public void register(RegisterParam param) {
        if (!captchaService.verifyCaptcha(param.getUuid(), param.getCode())) {
            throw new RocketPTException("验证码不正确");
        }
        if (!invitationService.check(param.getEmail(), param.getInvitationCode())) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage(
                    "invitation_not_exists"));
        }
        if (isExists(param.getEmail())) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage(
                    "email_exists"));
        }
         UserEntity userEntity = createUser(
                param.getUsername(), param.getNickname(), null, UserEntity.Gender.valueof(param.getSex()),
                param.getEmail(), UserEntity.State.INACTIVATED, 3L
        );
        String checkCode = checkCodeManager.generate(userEntity.getId(), userEntity.getEmail());
        userEntity.setCheckCode(checkCode);
        userEntity.setRegIp(IPUtils.getIpAddr());
        userEntity.setRegType(param.getType());
        updateById(userEntity);
        UserCredential userCredential = new UserCredential(
                userEntity.getId(),
                userCredentialService.generate(param.getUsername(), param.getPassword()),
                param.getUsername(),
                UserCredential.IdentityType.PASSWORD,
                passkeyManager.generate(userEntity.getId()),
                null
        );
        userCredentialService.save(userCredential);
        invitationService.consume(param.getEmail(), param.getInvitationCode(), userEntity);
    }


}
