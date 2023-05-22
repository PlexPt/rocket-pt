package com.rocketpt.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.CustomPage;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.entity.TorrentsEntity;
import com.rocketpt.server.service.infra.TorrentManager;
import com.rocketpt.server.service.TorrentsService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;


/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
@RestController
@Tag(name = "torrent种子相关", description = Constants.FinishStatus.FINISHED)
@RequiredArgsConstructor
@RequestMapping("/torrent")
@Validated
public class TorrentsController {

    private final TorrentsService torrentsService;
    private final TorrentManager torrentManager;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody CustomPage page) {
        Page<TorrentsEntity> entityPage = new Page<>(page.getCurrent(), page.getSize());
        entityPage.addOrder(page.getOrders());
        return Result.ok(torrentsService.page(entityPage));
    }

    /**
     * 信息
     */
    @PostMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        TorrentsEntity torrents = torrentsService.getById(id);
        return Result.ok(torrents);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody TorrentsEntity entity) {
        torrentsService.save(entity);

        return Result.ok();
    }

    /**
     * 保存/修改
     */
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody TorrentsEntity entity) {
        if (Objects.isNull(entity.getId())) {
            torrentsService.save(entity);
        } else {
            torrentsService.updateById(entity);
        }

        return Result.success();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(@RequestBody TorrentsEntity torrents) {
        torrentsService.updateById(torrents);

        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids) {
        torrentsService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

    /**
     * 上传
     */
    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile multipartFile,
                         @RequestPart("entity") TorrentsEntity torrentsEntity) {
        try {
            if (multipartFile.isEmpty()) {
                throw new RocketPTException(CommonResultStatus.PARAM_ERROR,
                        I18nMessage.getMessage("torrent_empty"));
            }
            byte[] bytes = multipartFile.getBytes();
            return torrentsService.upload(bytes, torrentsEntity);
        } catch (IOException e) {
            return Result.failure();
        }
    }

    @GetMapping("/download")
    public void download(@RequestParam("id") @Positive Integer id, HttpServletResponse response) throws IOException {
        Optional<TorrentsEntity> entityOptional = Optional.ofNullable(torrentsService.getById(id));
        if (entityOptional.isEmpty()) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage(
                    "torrent_not_exists"));
        }
        byte[] fetch = torrentManager.fetch(id);
        String filename = entityOptional.get().getFilename();
        filename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentLength(fetch.length);
        response.setContentType("application/x-bittorrent");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        if (response.isCommitted()) {
            return;
        }
        response.getOutputStream().write(fetch);
        response.getOutputStream().flush();
    }

}
