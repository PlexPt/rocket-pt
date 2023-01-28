package com.rocketpt.server.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.JsonUtils;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.sys.dto.PageDTO;
import com.rocketpt.server.sys.entity.Organization;
import com.rocketpt.server.sys.entity.User;
import com.rocketpt.server.sys.event.UserCreated;
import com.rocketpt.server.sys.event.UserDeleted;
import com.rocketpt.server.sys.event.UserUpdated;
import com.rocketpt.server.sys.repository.UserRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class UserService extends ServiceImpl<UserRepository, User> {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public User createUser(String username, String fullName, String avatar, User.Gender gender,
                           User.State state, Long organization) {
        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setAvatar(avatar);
        user.setGender(gender);
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

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId) {
        User user = findUserById(userId);
        removeById(user);
        DomainEventPublisher.instance().publish(new UserDeleted(user));
    }
}
