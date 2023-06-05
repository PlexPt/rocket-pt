package com.rocketpt.server.service.validator;

import com.rocketpt.server.dto.param.AnnounceRequest;

import org.springframework.stereotype.Component;


@Component
public class CheaterValidator implements TrackerValidator {


    @Override
    public void validate(AnnounceRequest request) {

        //todo 作弊检查 该部分是否需要开源？

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
