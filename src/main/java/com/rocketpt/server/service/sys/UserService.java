package com.rocketpt.server.service.sys;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.base.PageUtil;
import com.rocketpt.server.common.base.ResPage;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dao.UserDao;
import com.rocketpt.server.dto.entity.UserCredentialEntity;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.event.UserCreated;
import com.rocketpt.server.dto.event.UserDeleted;
import com.rocketpt.server.dto.event.UserUpdated;
import com.rocketpt.server.dto.param.*;
import com.rocketpt.server.dto.sys.UserinfoDTO;
import com.rocketpt.server.service.EmailCodeService;
import com.rocketpt.server.service.GoogleAuthenticatorService;
import com.rocketpt.server.service.infra.CheckCodeManager;
import com.rocketpt.server.service.infra.PasskeyManager;
import com.rocketpt.server.service.mail.MailService;
import com.rocketpt.server.util.IPUtils;
import com.rocketpt.server.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
    private final MailService mailService;
    private final EmailCodeService emailCodeService;
    private final UserRoleService userRoleService;
    private final RedisUtil redisUtil;

    private final GoogleAuthenticatorService googleAuthenticatorService;


    @Transactional(rollbackFor = Exception.class)
    public UserEntity createUser(String username,
                                 String fullName,
                                 String avatar,
                                 UserEntity.Gender gender,
                                 String email,
                                 UserEntity.State state) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setNickname(fullName);
        userEntity.setAvatar(avatar);
        userEntity.setGender(gender.getCode());
        userEntity.setEmail(email);
        userEntity.setState(state.getCode());
        userEntity.setCreateTime(LocalDateTime.now());
        save(userEntity);
        DomainEventPublisher.instance().publish(new UserCreated(userEntity));
        return userEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserEntity createUser(UserEntity entity) {
        entity.setState(0);
        entity.setCreateTime(LocalDateTime.now());
        save(entity);
        DomainEventPublisher.instance().publish(new UserCreated(entity));
        return entity;
    }

    public Set<UserEntity> findUserByIds(Set<Integer> userIds) {
        List<UserEntity> userEntities = listByIds(userIds);
        return new LinkedHashSet<>(userEntities);
    }

    public List<UserEntity> findUserByIds(List<Integer> ids) {
        List<UserEntity> list = listByIds(ids);
        return list;
    }

    public UserEntity findUserById(Integer userId) {
        UserEntity userEntity = getById(userId);
        if (userEntity == null) {
            throw new RocketPTException(RECORD_NOT_EXIST);
        }
        return userEntity;
    }


    @Transactional(rollbackFor = Exception.class)
    public UserEntity updateUser(Integer userId, String fullName, String avatar,
                                 UserEntity.Gender gender,
                                 UserEntity.State state, Long organization) {
        UserEntity userEntity = findUserById(userId);
        userEntity.setNickname(fullName);
        userEntity.setAvatar(avatar);
        userEntity.setGender(gender.getCode());
        userEntity.setState(state.getCode());
        updateById(userEntity);
        DomainEventPublisher.instance().publish(new UserUpdated(userEntity));
        return userEntity;
    }


    @Transactional(rollbackFor = Exception.class)
    public UserEntity updateUser(UserEntity entity) {

        updateById(entity);
        DomainEventPublisher.instance().publish(new UserUpdated(entity));
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserEntity lockUser(Integer userId) {
        UserEntity userEntity = findUserById(userId);
        userEntity.setState(UserEntity.State.LOCKED.getCode());
        updateById(userEntity);
        return userEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserEntity unlockUser(Integer userId) {
        UserEntity userEntity = findUserById(userId);
        userEntity.setState(UserEntity.State.NORMAL.getCode());
        updateById(userEntity);
        return userEntity;
    }

//    public PageDTO<UserEntity> findUsers(Pageable pageable, UserEntity userEntity) {
//        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
//        Map<String, Object> map = JsonUtils.parseToMap(JsonUtils.stringify(userEntity));
//        List<UserEntity> userEntities = listByMap(map);
//        long total = new PageInfo(userEntities).getTotal();
//        return new PageDTO<>(userEntities, total);
//    }

    public Result findUsers(UserParam param) {
        PageHelper.startPage(param.getPage(), param.getSize());
        boolean usernameNotEmpty = StringUtils.isNotEmpty(param.getUsername());
        List<UserEntity> list = list(new QueryWrapper<UserEntity>()
                .lambda()
                .like(usernameNotEmpty, UserEntity::getUsername, param.getUsername())
        );

        ResPage page = PageUtil.getPage(list);
        return Result.ok(list, page);
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
    public void delete(Integer userId) {
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
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterParam param) {
        if (param.getType() != 1) {
            // 检查邀请码是否有效
            if (!invitationService.check(param.getEmail(), param.getInvitationCode())) {
                throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage(
                        "invitation_not_exists"));
            }
        }

        // 检查邮箱和用户名是否已存在
        if (isExists(param.getEmail(), param.getUsername())) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage(
                    "email_exists"));
        }
        emailCodeService.checkEmailCode(param.getEmail(), param.getCode());

        // 校验通过，创建用户实体
        UserEntity userEntity = createUser(
                param.getUsername(),
                param.getNickname(),
                "/img.png",
                UserEntity.Gender.valueof(param.getSex()),
                param.getEmail(),
                UserEntity.State.NORMAL
        );

        // 设置用户属性
        updateById(userEntity);

        // 创建用户凭证实体
        UserCredentialEntity userCredentialEntity = new UserCredentialEntity();
        userCredentialEntity.setId(userEntity.getId());
        userCredentialEntity.setUsername(param.getUsername());
        userCredentialEntity.setRegIp(IPUtils.getIpAddr());
        userCredentialEntity.setRegType(param.getType());
        String checkCode = passkeyManager.generate(userEntity.getId());
        userCredentialEntity.setCheckCode(checkCode);

        // 生成随机盐和密码
        String salt = RandomUtil.randomString(8);
        String passkey = passkeyManager.generate(userEntity.getId());

        userCredentialEntity.setSalt(salt);
        userCredentialEntity.setPasskey(passkey);
        String generatedPassword = userCredentialService.generate(param.getPassword(), salt);
        userCredentialEntity.setPassword(generatedPassword);

        // 保存用户凭证实体
        userCredentialService.save(userCredentialEntity);
        // TODO 关联默认用户角色
        userRoleService.register(userEntity.getId());

        // 消费邀请码
        if (param.getType() != 1) {
            invitationService.consume(param.getEmail(), param.getInvitationCode(), userEntity);
        }
    }


    /**
     * 用户登录方法
     *
     * @param param 登录参数
     * @return 登录成功返回用户ID，登录失败返回0
     */
    public Integer login(LoginParam param) {
        String username = param.getUsername();
        String password = param.getPassword();

        // 根据用户名获取用户凭证实体
        UserCredentialEntity user = userCredentialService.getByUsername(username);

        if (user == null) {
            return 0;
        }

        // 对密码进行加密处理
        String encryptedPassword = SaSecureUtil.sha256(password + user.getSalt());

        // 比较加密后的密码与数据库中存储的密码是否一致
        if (!user.getPassword().equals(encryptedPassword)) {
            return 0;
        }

        // 获取用户的TOTP
        String totp = user.getTotp();

        // 验证TOTP码是否有效
        boolean codeValid = isTotpValid(param.getTotp(), totp);
        if (!codeValid) {
            return 0;
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
    public boolean isUserLocked(Integer userId) {
        UserEntity userEntity = getOne(new QueryWrapper<UserEntity>()
                .lambda()
                .select(UserEntity::getState)
                .eq(UserEntity::getId, userId)
        );
        if (userEntity == null) {
            throw new UserException(CommonResultStatus.UNAUTHORIZED, "用户不存在");
        }

        return userEntity.getState() == 1;
    }

    /**
     * 验证TOTP码是否有效
     *
     * @param verificationCode 用户输入的验证码
     * @param totp             用户的TOTP码
     * @return 验证码有效返回true，无效返回false
     */
    public boolean isTotpValid(Integer verificationCode, String totp) {
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

    /**
     * 根据email获取用户信息
     *
     * @return
     */
    private UserEntity getByEmail(String email) {
        return getOne(new QueryWrapper<UserEntity>()
                .lambda()
                .eq(UserEntity::getEmail, email), false
        );
    }

    /**
     * 获取当前登录的用户ID
     */
    public Integer getUserId() {
        return StpUtil.getLoginIdAsInt();
    }


    /**
     * 获取用户信息
     */
    public String getUsername(int userId) {
        UserEntity entity = getOne(new QueryWrapper<UserEntity>()
                .lambda()
                .select(UserEntity::getUsername)
                .eq(UserEntity::getId, userId)
        );
        return entity.getUsername();
    }

    /**
     * 获取用户信息
     */
    public UserinfoDTO getUserInfo() {
        //TODO 校验用户信息，并只需要返回必要信息，隐藏敏感信息
        //获取用户信息
        UserEntity userEntity = findUserById(getUserId());
        UserinfoDTO userinfoDTO = new UserinfoDTO();
        BeanUtils.copyProperties(userEntity, userinfoDTO);
        return userinfoDTO;
    }


    /**
     * 确认邮箱
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirm(String code) {
        UserCredentialEntity userCredential = userCredentialService.getByCheckCode(code);
        if (userCredential == null) {
            throw new RocketPTException("校验码不正确");
        }
        Integer id = userCredential.getId();
        UserEntity entity = getById(id);
        if (!entity.getState().equals(2)) {
            throw new RocketPTException("用户状态不正确");
        }

        entity.setState(0);
        updateById(entity);
        userCredentialService.resetCheckCode(id);

    }


    /**
     * 忘记密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void forgotPassword(ForgotPasswordParam param) {
        UserEntity entity = getByEmail(param.getEmail());
        if (entity == null) {
            throw new RocketPTException("邮箱不正确");
        }

        if (!entity.getState().equals(0)) {
            throw new RocketPTException("用户状态不正确");
        }
        String checkCode = userCredentialService.resetCheckCode(entity.getId());
        //发邮件
        mailService.sendMail(param.getEmail(),
                I18nMessage.getMessage("confirm_title"),
                I18nMessage.getMessage("confirm_email") + checkCode,
                null);
    }


    /**
     * 更新密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(ChangePasswordParam param) {
        // 获取用户凭证实体
        Integer userId = getUserId();
        UserCredentialEntity credentialEntity = userCredentialService.getById(userId);

        // 生成旧密码的哈希值
        String old = userCredentialService.generate(param.getOldPassword(),
                credentialEntity.getSalt());

        // 获取数据库中保存的密码
        String password = credentialEntity.getPassword();

        // 检查旧密码是否正确
        if (!old.equals(password)) {
            // 抛出用户异常，表示未授权访问
            throw new UserException(CommonResultStatus.UNAUTHORIZED, "密码不正确");
        }

        // 更新用户凭证实体中的密码
        userCredentialService.updatePassword(userId, param.getNewPassword(),
                credentialEntity.getSalt());

    }


    /**
     * 重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(ResetPasswordParam param) {
        String code = param.getCheckCode();
        UserCredentialEntity userCredential = userCredentialService.getByCheckCode(code);
        if (userCredential == null) {
            throw new RocketPTException("校验码不正确");
        }
        Integer userId = userCredential.getId();
        UserEntity entity = getById(userId);
        if (!entity.getState().equals(0)) {
            throw new RocketPTException("用户状态不正确");
        }

        // 更新用户凭证实体中的密码
        userCredentialService.updatePassword(userId, param.getNewPassword(),
                userCredential.getSalt());

        userCredentialService.resetCheckCode(userId);

    }


    public String getPasskey(Integer id) {
        UserCredentialEntity entity =
                userCredentialService.getOne(new QueryWrapper<UserCredentialEntity>()
                        .lambda()
                        .select(UserCredentialEntity::getPasskey)
                        .eq(UserCredentialEntity::getId, id)
                );
        return entity.getPasskey();
    }

    /**
     * 验证图片验证码和邀请码正确后发送注册验证码邮件
     *
     * @param param
     */
    public void sendRegCode(RegisterCodeParam param) {
        //TODO  判断处理注册类型
        //      1.开放注册
        //      2.受邀注册
        //      3.自助答题注册
        //验证图片验证码和邀请码正确后发送邮件
        if (!captchaService.verifyCaptcha(param.getUuid(), param.getCode())) {
            throw new RocketPTException("图片验证码错误");
        }
        if (param.getType() != 1) {
            // 检查邀请码是否有效
            if (!invitationService.check(param.getEmail(), param.getInvitationCode())) {
                throw new RocketPTException("邀请码错误");
            }
        }
        emailCodeService.setMailCode(param.getEmail());

    }
}
