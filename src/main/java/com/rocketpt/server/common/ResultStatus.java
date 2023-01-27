package com.rocketpt.server.common;

/**
 * @author plexpt
 */
public interface ResultStatus {
    /**
     * 错误码
     */
    int getCode();

    /**
     * 错误信息
     */
    String getMessage();
}
