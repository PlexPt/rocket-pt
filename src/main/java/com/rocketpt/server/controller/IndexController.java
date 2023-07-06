package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.config.SystemConfigKeys;
import com.rocketpt.server.dto.SiteConfig;
import com.rocketpt.server.service.sys.SystemConfigService;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "首页相关", description = Constants.FinishStatus.FINISHED)
@RequiredArgsConstructor
@RestController
public class IndexController {

    final SystemConfigService sysConfigService;

    final BuildProperties buildProperties;

    //        final GitVersionProperties gitVersionProperties;
    final GitProperties gitProperties;


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


    @ApiResponse(responseCode = "0", description = "操作成功",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = SiteConfig.class))
            })
    @Operation(summary = "获取站点配置", description = "获取站点配置并缓存起来")
    @GetMapping("/site-config")
    public Result siteConfig() {
        SiteConfig siteConfig = sysConfigService.get(SystemConfigKeys.SITE_CONFIG,
                SiteConfig.class);

        return Result.ok(siteConfig);
    }

}
