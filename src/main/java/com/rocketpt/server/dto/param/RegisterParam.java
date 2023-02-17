package com.rocketpt.server.dto.param;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 注册参数
 */
@Data
public class RegisterParam {

    /**
     * 注册类型
     * <p>
     * 1.开放注册
     * 2.受邀注册
     * 3.自助答题注册
     */
    private Integer type;

    private String username;

    private String nickname;

    private String email;

    private String password;

    /**
     * 验证码uuid
     */
    @NotNull
    private String uuid;

    /**
     * 验证码
     */
    @NotNull
    private String code;

    /**
     * 邀请码
     */
    private String invitationCode;

    /**
     * 国家地区
     */
    private String country;

    /**
     * 性别
     * 0 男
     * 1 女
     * 2 其他
     */

    private Integer sex;
}
