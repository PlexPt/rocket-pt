package com.rocketpt.server.sys.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.entity.Organization;
import com.rocketpt.server.dto.entity.User;
import com.rocketpt.server.dto.entity.UserCredential;
import com.rocketpt.server.dto.event.UserCreated;
import com.rocketpt.server.dto.event.UserDeleted;
import com.rocketpt.server.dto.event.UserUpdated;
import com.rocketpt.server.dto.param.RegisterParam;
import com.rocketpt.server.dto.sys.PageDTO;
import com.rocketpt.server.infra.service.CheckCodeManager;
import com.rocketpt.server.infra.service.PasskeyManager;
import com.rocketpt.server.sys.repository.UserRepository;
import com.rocketpt.server.util.IPUtils;
import com.rocketpt.server.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.rocketpt.server.common.CommonResultStatus.RECORD_NOT_EXIST;

/**
 * @author plexpt
 */
@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserRepository, User> {

    private final UserRepository userRepository;
    private final InvitationService invitationService;
    private final UserCredentialService userCredentialService;
    private final CheckCodeManager checkCodeManager;
    private final PasskeyManager passkeyManager;
    private final CaptchaService captchaService;

    @Transactional(rollbackFor = Exception.class)
    public User createUser(String username, String fullName, String avatar, User.Gender gender,
                           String email, User.State state, Long organization) {
        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setAvatar(avatar);
        user.setGender(gender);
        user.setEmail(email);
        user.setState(state);
        user.setCreatedTime(LocalDateTime.now());
        user.setOrganizationId(organization);
        save(user);
        DomainEventPublisher.instance().publish(new UserCreated(user));
        return user;
    }

    public Set<User> findUserByIds(Set<Long> userIds) {
        List<User> users = listByIds(userIds);
        return new LinkedHashSet<>(users);
    }

    public User findUserById(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new RocketPTException(RECORD_NOT_EXIST);
        }
        return user;
    }

    public PageDTO<User> findOrgUsers(Pageable pageable, String username, User.State state,
                                      Organization organization) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<User> users = userRepository.findOrgUsers(pageable, username, state,
                organization.getId(),
                organization.makeSelfAsParentIds());
        long total = new PageInfo(users).getTotal();
        return new PageDTO<>(users, total);
    }

    public boolean existsUsers(Organization organization) {
        String orgParentIds = organization.makeSelfAsParentIds();
        return userRepository.countOrgUsers(organization.getId(), orgParentIds) > 0;
    }


    @Transactional(rollbackFor = Exception.class)
    public User updateUser(Long userId, String fullName, String avatar, User.Gender gender,
                           User.State state, Long organization) {
        User user = findUserById(userId);
        user.setFullName(fullName);
        user.setAvatar(avatar);
        user.setGender(gender);
        user.setState(state);
        user.setOrganizationId(organization);
        updateById(user);
        DomainEventPublisher.instance().publish(new UserUpdated(user));
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    public User lockUser(Long userId) {
        User user = findUserById(userId);
        user.setState(User.State.LOCKED);
        updateById(user);
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    public User unlockUser(Long userId) {
        User user = findUserById(userId);
        user.setState(User.State.NORMAL);
        updateById(user);
        return user;
    }

    public PageDTO<User> findUsers(Pageable pageable, User user) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        Map<String, Object> map = JsonUtils.parseToMap(JsonUtils.stringify(user));
        List<User> users = listByMap(map);
        long total = new PageInfo(users).getTotal();
        return new PageDTO<>(users, total);
    }

    public boolean isExists(String email) {
        return count(Wrappers.<User>lambdaQuery()
                .eq(User::getEmail, email)
        ) != 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId) {
        User user = findUserById(userId);
        removeById(user);
        DomainEventPublisher.instance().publish(new UserDeleted(user));
    }

    @Transactional(rollbackFor = SQLException.class)
    public void register(RegisterParam param) {
        if (!captchaService.verifyCaptcha(param.getUuid(), param.getCode()))
            throw new RocketPTException("验证码不正确");
        if (!invitationService.check(param.getEmail(), param.getInvitationCode()))
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage("invitation_not_exists"));
        if (isExists(param.getEmail()))
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage("email_exists"));
        User user = createUser(
                param.getUsername(), param.getNickname(), null, User.Gender.valueof(param.getSex()),
                param.getEmail(), User.State.INACTIVATED, 3L
        );
        String checkCode = checkCodeManager.generate(user.getId(), user.getEmail());
        user.setCheckCode(checkCode);
        user.setRegIp(IPUtils.getIpAddr());
        user.setRegType(param.getType());
        updateById(user);
        UserCredential userCredential = new UserCredential(
                user.getId(),
                userCredentialService.generate(param.getUsername(), param.getPassword()),
                param.getUsername(),
                UserCredential.IdentityType.PASSWORD,
                passkeyManager.generate(user.getId()),
                null
        );
        userCredentialService.save(userCredential);
        invitationService.consume(param.getEmail(), param.getInvitationCode(), user);
    }


}
