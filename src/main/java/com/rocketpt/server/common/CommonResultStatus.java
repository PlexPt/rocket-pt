package com.rocketpt.server.common;

/**
 * @author plexpt
 */
public enum CommonResultStatus implements ResultStatus {

    OK(0, "成功"),

    FAIL(500, "失败"),

    PARAM_ERROR(400, "参数非法"),

    RECORD_NOT_EXIST(404, "记录不存在"),

    UNAUTHORIZED(401, "未授权"),

    FORBIDDEN(403, "无权限"),

    SERVER_ERROR(500, "服务器内部错误");

    private final int code;
    private final String message;

    CommonResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
