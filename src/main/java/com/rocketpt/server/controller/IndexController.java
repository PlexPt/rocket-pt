package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "首页相关", description = Constants.FinishStatus.FINISHED)
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

    @Operation(summary = "获取地区专业列表")
    @GetMapping("/info")
    public Result info() {
        //TODO
        return Result.ok();
    }

}
