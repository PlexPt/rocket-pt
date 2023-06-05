package com.rocketpt.server.service.validator;


import com.rocketpt.server.dto.param.AnnounceRequest;

import org.springframework.stereotype.Component;

@Component
public class NumWantValidator implements TrackerValidator {


    final int defaultNumWant = 200;
    final int maxNunWant = 2000;

    @Override
    public void validate(AnnounceRequest request) {
        // 设置默认值
        if (request.getNumWant().equals(-1)) {
            request.setNumWant(defaultNumWant);
            return;
        }

        // 小于0
        request.setNumWant(Math.max(0, request.getNumWant()));

        // 大于最大值
        request.setNumWant(Math.min(request.getNumWant(), maxNunWant));

    }

    @Override
    public int getOrder() {
        return 1;
    }

}
