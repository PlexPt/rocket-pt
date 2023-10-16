package com.rocketpt.server.service.validator;

import com.rocketpt.server.common.exception.TrackerException;
import com.rocketpt.server.common.exception.TrackerNoRetryException;
import com.rocketpt.server.dto.param.AnnounceRequest;
import org.springframework.stereotype.Component;


@Component
public class PasskeyValidator implements TrackerValidator {


    @Override
    public void validate(AnnounceRequest request) {

        //todo 验证Passkey, 增加签名算法
        int length = request.getPasskey().length();
        if (length > 64 || length < 16) {
            throw new TrackerNoRetryException("Invalid passkey. QAQ");
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
