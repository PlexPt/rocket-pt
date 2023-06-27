package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dao.InvitationDao;
import com.rocketpt.server.dto.entity.InvitationEntity;
import com.rocketpt.server.dto.entity.UserEntity;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationService extends ServiceImpl<InvitationDao, InvitationEntity> {

    public boolean check(String inviteeEmail, String invitationCode) {
        Optional<InvitationEntity> invitationOptional = Optional.ofNullable(
                getOne(
                        Wrappers.<InvitationEntity>lambdaQuery()
                                .eq(InvitationEntity::getInviteeEmail, inviteeEmail)
                                .eq(InvitationEntity::getHash, invitationCode)
                                .eq(InvitationEntity::getValid, 1)
                )
        );
        return invitationOptional.isPresent();
    }

    /**
     * 消费邀请码
     *
     * @param inviteeEmail
     * @param invitationCode
     * @param userEntity
     */
    public void consume(String inviteeEmail, String invitationCode, UserEntity userEntity) {
        InvitationEntity invitationEntity = getOne(
                Wrappers.<InvitationEntity>lambdaQuery()
                        .eq(InvitationEntity::getInviteeEmail, inviteeEmail)
                        .eq(InvitationEntity::getHash, invitationCode)
                        .eq(InvitationEntity::getValid, 1)
        );
        invitationEntity.setUsername(userEntity.getUsername());
        invitationEntity.setUserId(userEntity.getId());
        invitationEntity.setRegTime(LocalDateTime.now());
        invitationEntity.setValid(false);
        updateById(invitationEntity);
    }

    public List<InvitationEntity> listInvitationListByUserId(Integer userId) {
        List<InvitationEntity> list = list(
                Wrappers.<InvitationEntity>lambdaQuery()
                        .eq(InvitationEntity::getInviter, userId)
        );

        return list;
    }
}
