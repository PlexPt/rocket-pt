package com.rocketpt.server.common.exception;


public class TrackerNoRetryException extends TrackerException {

    public TrackerNoRetryException() {
        super();
    }

    public TrackerNoRetryException(String message) {
        super(message);
    }

    public TrackerNoRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrackerNoRetryException(Throwable cause) {
        super(cause);
    }

    protected TrackerNoRetryException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
