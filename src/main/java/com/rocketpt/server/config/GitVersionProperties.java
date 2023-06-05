package com.rocketpt.server.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;
import lombok.NoArgsConstructor;

//@Configuration
@ConfigurationProperties(prefix = "git", ignoreUnknownFields = false)
@PropertySource("classpath:git.properties")
@Data
@NoArgsConstructor
public class GitVersionProperties {

    //git.branch=main
    //git.build.host=8086K
    //git.build.time=2023-05-31 00\:31\:53
    //git.build.user.email=xxx@gmail.com
    //git.build.user.name=pt
    //git.build.version=1.0.0
    //git.closest.tag.commit.count=
    //git.closest.tag.name=
    //git.commit.author.time=2023-05-30 11\:07\:49
    //git.commit.committer.time=2023-05-30 11\:07\:49
    //git.commit.id=2a4c0c57cd0cfc341e14d407797244efca893eee
    //git.commit.id.abbrev=2a4c0c5
    //git.commit.id.describe=2a4c0c5-dirty
    //git.commit.id.describe-short=2a4c0c5-dirty
    //git.commit.message.full=\u79CD\u5B50\u6807\u7B7E
    //git.commit.message.short=\u79CD\u5B50\u6807\u7B7E
    //git.commit.time=2023-05-30 11\:07\:49
    //git.commit.user.email=plexpt@gmail.com
    //git.commit.user.name=pt
    //git.dirty=true
    //git.local.branch.ahead=0
    //git.local.branch.behind=0
    //git.remote.origin.url=git@github.com\:PlexPt/rocket-pt.git
    //git.tags=
    //git.total.commit.count=70

    private String branch;
//    private String buildHost;
//    private String buildTime;
//    private String buildUserEmail;
//    private String buildUserName;
//    private String buildVersion;
//    private String closestTagCommitCount;
//    private String closestTagName;
//    private String commitAuthorTime;
//    private String commitCommitterTime;
//    private String commitId;
//    private String commitIdAbbrev;
//    private String commitIdDescribe;
//    private String commitIdDescribeShort;
//    private String commitMessageFull;
//    private String commitMessageShort;
//    private String commitTime;
//    private String commitUserEmail;
//    private String commitUserName;
//    private Boolean dirty;
//    private Integer localBranchAhead;
//    private Integer localBranchBehind;
//    private String remoteOriginUrl;
//    private String tags;
//    private Integer totalCommitCount;
}
