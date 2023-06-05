package com.rocketpt.server.service.validator;

import com.rocketpt.server.dto.param.AnnounceRequest;

import org.springframework.stereotype.Component;


@Component
public class UserAgentValidator implements TrackerValidator {


    @Override
    public void validate(AnnounceRequest request) {

        //todo 验证客户端UserAgent 只允许白名单


    }

    @Override
    public int getOrder() {
        return 3;
    }
}
