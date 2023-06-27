package com.rocketpt.server.service.validator;

import com.rocketpt.server.common.exception.TrackerException;
import com.rocketpt.server.dao.UserDao;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.param.AnnounceRequest;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class UserValidator implements TrackerValidator {


    final UserDao userDao;

    @Override
    public void validate(AnnounceRequest request) {

        UserEntity user = userDao.findUserByPasskey(request.getPasskey());

        if (user == null) {
            throw new TrackerException("passkey invalid, pls redownload the torrent file.");
        }


        if (!user.isUserOK()) {
            throw new TrackerException("user invalid");
        }


        request.setUser(user);

    }

    @Override
    public int getOrder() {
        return 100;
    }


}
