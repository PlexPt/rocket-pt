package com.rocketpt.server.service.validator;

import com.rocketpt.server.dto.param.AnnounceRequest;

public interface TrackerValidator {

    /**
     * 验证
     *
     * @param object
     */
    void validate(AnnounceRequest object);

    /**
     * 优先校验非IO操作
     *
     * @return 执行顺序，小的先执行
     */
    default int getOrder() {
        return 0;
    }
}
