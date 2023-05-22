package com.rocketpt.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;

import cn.dev33.satoken.SaManager;
import lombok.SneakyThrows;

@EnableCaching
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class RocketServerApplication implements CommandLineRunner {

    @Autowired
    private Environment environment;

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RocketServerApplication.class, args);
    }


    @SneakyThrows
    public void init() {
        String serverPort = environment.getProperty("server.port");
        String localIP = InetAddress.getLocalHost().getHostAddress();
        String publicIP = restTemplate.getForObject("https://api.ip.sb/ip", String.class).trim();
        String serverAddress = "http://" + localIP + ":" + serverPort;
        String apiDocAddress = "http://localhost:" + serverPort + "/doc.html";
        String apiDocAddressPublic = "http://" + publicIP + ":" + serverPort + "/doc.html";

        System.out.println("\n===============================================");
        System.out.printf("服务器已启动，地址为： %s\n", serverAddress);
        System.out.printf("API文档地址为： %s\n", apiDocAddress);
        System.out.printf("API文档公网地址为： %s\n", apiDocAddressPublic);
        System.out.printf("本地IP地址为： %s\n", localIP);
        System.out.printf("公共IP地址为： %s\n", publicIP);
        System.out.println("Sa-Token配置如下：" + SaManager.getConfig());
        System.out.println("GitHub：https://github.com/plexpt/rocket-pt");
        System.out.println("=================================================\n");
    }

    @Override
    public void run(String... args) throws Exception {
        init();
    }
}
