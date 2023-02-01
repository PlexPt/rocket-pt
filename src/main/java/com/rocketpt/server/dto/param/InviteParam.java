package com.rocketpt.server.dto.param;


import lombok.Data;

/**
 * 注册参数
 */
@Data
public class InviteParam {

    private String username;

    private String email;

    private String content;

    private String remark;

}
