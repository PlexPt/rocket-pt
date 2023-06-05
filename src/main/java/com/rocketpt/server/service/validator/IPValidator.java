package com.rocketpt.server.service.validator;

import com.rocketpt.server.dto.param.AnnounceRequest;

import org.springframework.stereotype.Component;


@Component
public class IPValidator implements TrackerValidator {


    @Override
    public void validate(AnnounceRequest request) {

        //todo 验证IP

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
