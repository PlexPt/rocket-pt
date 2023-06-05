package com.rocketpt.server.service.validator;

import com.rocketpt.server.dto.param.AnnounceRequest;

import org.springframework.stereotype.Component;


@Component
public class DuplicatedAnnounceValidator implements TrackerValidator {


    @Override
    public void validate(AnnounceRequest request) {

        //todo 验证重复请求

    }

    @Override
    public int getOrder() {
        return 102;
    }
}
