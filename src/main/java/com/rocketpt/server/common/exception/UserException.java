package com.rocketpt.server.common.exception;

import com.rocketpt.server.common.ResultStatus;

/**
 * @author plexpt
 */
public class UserException extends RocketPTException {

    public UserException(ResultStatus status) {
        super(status);
    }

    public UserException(ResultStatus status, String message) {
        super(status, message);
    }
}
