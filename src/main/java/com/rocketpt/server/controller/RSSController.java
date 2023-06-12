package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.dao.SuggestDao;
import com.rocketpt.server.service.TorrentService;
import com.rocketpt.server.service.sys.UserService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@Tag(name = "种子rss相关", description = Constants.FinishStatus.UNFINISHED)
@RequiredArgsConstructor
@RequestMapping("/rss")
@Validated
public class RSSController {

    private final TorrentService torrentService;

    private final UserService userService;


}
