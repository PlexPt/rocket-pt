package com.rocketpt.server.sys.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dto.entity.Invitation;
import com.rocketpt.server.dto.entity.User;
import com.rocketpt.server.sys.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationService extends ServiceImpl<InvitationRepository, Invitation> {

    Boolean check(String inviteeEmail, String invitationCode) {
        Optional<Invitation> invitationOptional = Optional.ofNullable(
                getOne(
                        Wrappers.<Invitation>lambdaQuery()
                                .eq(Invitation::getInviteeEmail, inviteeEmail)
                                .eq(Invitation::getHash, invitationCode)
                                .eq(Invitation::getValid, 1)
                )
        );
        return invitationOptional.isPresent();
    }

    void consume(String inviteeEmail, String invitationCode, User user) {
        Invitation invitation = getOne(
                Wrappers.<Invitation>lambdaQuery()
                        .eq(Invitation::getInviteeEmail, inviteeEmail)
                        .eq(Invitation::getHash, invitationCode)
                        .eq(Invitation::getValid, 1)
        );
        invitation.setUsername(user.getUsername());
        invitation.setUserId(user.getId());
        invitation.setRegTime(new Date());
        invitation.setValid(false);
        updateById(invitation);
    }

}
