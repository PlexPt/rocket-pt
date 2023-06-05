package com.rocketpt.server.service.validator;

import com.rocketpt.server.common.exception.TrackerException;
import com.rocketpt.server.dto.param.AnnounceRequest;

import org.springframework.stereotype.Component;


@Component
public class PasskeyValidator implements TrackerValidator {


    @Override
    public void validate(AnnounceRequest request) {

        //todo 验证Passkey, 增加签名算法
        if (request.getPasskey().length() != 32) {
            throw new TrackerException("Invalid passkey. QAQ");
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
