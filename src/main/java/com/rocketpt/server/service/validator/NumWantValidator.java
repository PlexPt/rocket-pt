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
        if (request.getNumwant() == null || request.getNumwant().equals(-1)) {
            request.setNumwant(defaultNumWant);
            return;
        }

        // 小于0
        request.setNumwant(Math.max(0, request.getNumwant()));

        // 大于最大值
        request.setNumwant(Math.min(request.getNumwant(), maxNunWant));

    }

    @Override
    public int getOrder() {
        return 1;
    }

}
