package com.rocketpt.server.controller;

import com.rocketpt.server.config.GitVersionProperties;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class IndexController {

    final BuildProperties buildProperties;

//    final GitVersionProperties gitVersionProperties;
    final GitProperties gitProperties;

//    @Value("${git.commit.id.abbrev}")
//    private String commitHash;

    @GetMapping("/")
    public Object index() {

        String version = buildProperties.getVersion();
        String res = "version = " + version + "\n" +
                "commitHash = " + gitProperties.getShortCommitId();


        return res;
    }
}
