package com.rocketpt.server.service.mail;


import lombok.Data;

@Data
public class SmtpConfig {


    private String host;
    private Integer port;
    private String username;
    private String password;
    private String protocol;

}
