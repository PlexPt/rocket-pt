package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dao.UserDao;
import com.rocketpt.server.dto.entity.OrganizationEntity;
import com.rocketpt.server.dto.entity.UserCredentialEntity;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.event.UserCreated;
import com.rocketpt.server.dto.event.UserDeleted;
import com.rocketpt.server.dto.event.UserUpdated;
import com.rocketpt.server.dto.param.LoginParam;
import com.rocketpt.server.dto.param.RegisterParam;
import com.rocketpt.server.dto.sys.PageDTO;
import com.rocketpt.server.dto.sys.UserinfoDTO;
import com.rocketpt.server.service.GoogleAuthenticatorService;
import com.rocketpt.server.service.infra.CheckCodeManager;
import com.rocketpt.server.service.infra.PasskeyManager;
import com.rocketpt.server.util.IPUtils;
import com.rocketpt.server.util.JsonUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
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

    private final GoogleAuthenticatorService googleAuthenticatorService;

    @Transactional(rollbackFor = Exception.class)
    public UserEntity createUser(String username,
                                 String fullName,
                                 String avatar,
                                 UserEntity.Gender gender,
                                 String email,
                                 UserEntity.State state,
                                 Long organization) {
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

    public PageDTO<UserEntity> findOrgUsers(Pageable pageable, String username,
                                            UserEntity.State state,
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
    public UserEntity updateUser(Long userId, String fullName, String avatar,
                                 UserEntity.Gender gender,
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

    public boolean isExists(String email, String username) {
        long count = count(Wrappers.<UserEntity>lambdaQuery()
                .eq(UserEntity::getEmail, email)
                .or()
                .eq(UserEntity::getUsername, username)
        );

        return count > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId) {
        UserEntity userEntity = findUserById(userId);
        removeById(userEntity);
        DomainEventPublisher.instance().publish(new UserDeleted(userEntity));
    }

    /**
     * 用户注册方法
     *
     * @param param 注册参数
     * @throws RocketPTException 注册过程中的异常
     */
    @Transactional(rollbackFor = SQLException.class)
    public void register(RegisterParam param) {

        // 检查邀请码是否有效
        if (!invitationService.check(param.getEmail(), param.getInvitationCode())) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage(
                    "invitation_not_exists"));
        }

        // 检查邮箱和用户名是否已存在
        if (isExists(param.getEmail(), param.getUsername())) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage(
                    "email_exists"));
        }

        // 校验通过，创建用户实体
        UserEntity userEntity = createUser(
                param.getUsername(),
                param.getNickname(),
                null,
                UserEntity.Gender.valueof(param.getSex()),
                param.getEmail(),
                UserEntity.State.INACTIVATED,
                3L
        );

        // 生成邮件验证码并设置用户属性
        userEntity.setRegIp(IPUtils.getIpAddr());
        userEntity.setRegType(param.getType());
        updateById(userEntity);

        // 创建用户凭证实体
        UserCredentialEntity userCredentialEntity = new UserCredentialEntity();
        userCredentialEntity.setId(userEntity.getId());
        userCredentialEntity.setUsername(param.getUsername());
        String checkCode = passkeyManager.generate(userEntity.getId());
        userEntity.setCheckCode(checkCode);

        // 生成随机盐和密码
        String salt = RandomUtil.randomString(8);
        String passkey = passkeyManager.generate(userEntity.getId());

        userCredentialEntity.setSalt(salt);
        userCredentialEntity.setPasskey(passkey);
        String generatedPassword = userCredentialService.generate(param.getPassword(), salt);
        userCredentialEntity.setPassword(generatedPassword);

        // 保存用户凭证实体
        userCredentialService.save(userCredentialEntity);

        // 消费邀请码
        invitationService.consume(param.getEmail(), param.getInvitationCode(), userEntity);
        //TODO 发邮件
    }


    /**
     * 用户登录方法
     *
     * @param param 登录参数
     * @return 登录成功返回用户ID，登录失败返回0
     */
    public Long login(LoginParam param) {
        String username = param.getUsername();
        String password = param.getPassword();

        // 根据用户名获取用户凭证实体
        UserCredentialEntity user = userCredentialService.getByUsername(username);

        if (user == null) {
            return 0L;
        }

        // 对密码进行加密处理
        String encryptedPassword = SaSecureUtil.sha256(password + user.getSalt());

        // 比较加密后的密码与数据库中存储的密码是否一致
        if (!user.getPassword().equals(encryptedPassword)) {
            return 0L;
        }

        // 获取用户的TOTP
        String totp = user.getTotp();

        // 验证TOTP码是否有效
        boolean codeValid = isTotpValid(param.getTotp(), totp);
        if (!codeValid) {
            return 0L;
        }

        if (isUserLocked(user.getId())) {
            throw new UserException(CommonResultStatus.UNAUTHORIZED, "用户已经禁用，请与管理员联系");
        }

        // 返回用户ID
        return user.getId();
    }

    /**
     * @param userId
     * @return 用户是否已经禁用
     */
    public boolean isUserLocked(long userId) {
        UserEntity userEntity = getOne(new QueryWrapper<UserEntity>()
                .lambda()
                .select(UserEntity::getState)
                .eq(UserEntity::getId, userId)
        );

        return userEntity.getState() == 1;
    }

    /**
     * 验证TOTP码是否有效
     *
     * @param verificationCode 用户输入的验证码
     * @param totp             用户的TOTP码
     * @return 验证码有效返回true，无效返回false
     */
    private boolean isTotpValid(Integer verificationCode, String totp) {
        // 如果用户的TOTP码为空，视为有效
        if (StringUtils.isEmpty(totp)) {
            return true;
        }

        // 如果用户输入的验证码为空，视为无效
        if (verificationCode == null) {
            return false;
        }

        // 使用Google Authenticator服务验证TOTP码的有效性
        boolean codeValid = googleAuthenticatorService.isCodeValid(totp, verificationCode);

        // 返回验证结果
        return codeValid;
    }


    /**
     * 根据username获取用户信息
     *
     * @param username
     * @return
     */
    private UserEntity getByUsername(String username) {
        return getOne(new QueryWrapper<UserEntity>()
                .lambda()
                .eq(UserEntity::getUsername, username), false
        );
    }

    public Long getUserId() {
        return StpUtil.getLoginIdAsLong();
    }


    public UserinfoDTO getUserInfo() {
        return null;
    }

    public void confirm(String code) {
        UserCredentialEntity userCredential = userCredentialService.getByCheckCode(code);
        if (userCredential == null) {
            throw new RocketPTException("校验码不正确");
        }
        Long id = userCredential.getId();
        UserEntity entity = getById(id);
        if (!entity.getState().equals(2)) {
            throw new RocketPTException("用户状态不正确");
        }

        entity.setState(0);
        updateById(entity);

    }
}
