package com.rocketpt.server.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "pt")
public class PTProperties {


    /**
     * 定时任务 启动任务相关
     */
    Task task;

    String host = "";


    @Data
    public static class Task {

        /**
         * 每日分析
         */
        boolean analyze = false;

    }

}
