package com.rocketpt.server.common.exception;

import com.rocketpt.server.common.ResultStatus;

/**
 * @author plexpt
 */
public class RocketPTException extends RuntimeException {
    private final ResultStatus status;

    public RocketPTException(ResultStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    public RocketPTException(ResultStatus status, String message) {
        super(message);
        this.status = status;
    }

    public ResultStatus getStatus() {
        return status;
    }


}
