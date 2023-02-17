package com.rocketpt.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.base.CustomPage;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.base.Res;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.infra.service.TorrentManager;
import com.rocketpt.server.web.entity.TorrentsEntity;
import com.rocketpt.server.web.service.TorrentsService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
@RestController
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
    public Res list(@RequestBody CustomPage page) {
        Page<TorrentsEntity> entityPage = new Page<>(page.getCurrent(), page.getSize());
        entityPage.addOrder(page.getOrders());
        return Res.ok(torrentsService.page(entityPage));
    }

    /**
     * 信息
     */
    @PostMapping("/info/{id}")
    public Res info(@PathVariable("id") Integer id) {
        TorrentsEntity torrents = torrentsService.getById(id);

        return Res.ok(torrents);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Res save(@RequestBody TorrentsEntity entity) {
        torrentsService.save(entity);

        return Res.ok();
    }

    /**
     * 保存/修改
     */
    @PostMapping("/saveOrUpdate")
    public Res saveOrUpdate(@RequestBody TorrentsEntity entity) {
        if (Objects.isNull(entity.getId())) {
            torrentsService.save(entity);
        } else {
            torrentsService.updateById(entity);
        }

        return Res.success();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Res update(@RequestBody TorrentsEntity torrents) {
        torrentsService.updateById(torrents);

        return Res.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Res delete(@RequestBody Integer[] ids) {
        torrentsService.removeByIds(Arrays.asList(ids));

        return Res.ok();
    }

    /**
     * 上传
     */
    @PostMapping("/upload")
    public Res upload(@RequestPart("file") MultipartFile multipartFile, @RequestPart("entity") TorrentsEntity torrentsEntity) {
        try {
            if (multipartFile.isEmpty()) throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage("torrent_empty"));
            byte[] bytes = multipartFile.getBytes();
            return torrentsService.upload(bytes, torrentsEntity);
        } catch (IOException e) {
            return Res.failure();
        }
    }

    @GetMapping("/download")
    public void download(@RequestParam("id") @Positive Integer id, HttpServletResponse response) throws IOException {
        Optional<TorrentsEntity> entityOptional = Optional.ofNullable(torrentsService.getById(id));
        if (entityOptional.isEmpty()) throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage("torrent_not_exists"));
        byte[] fetch = torrentManager.fetch(id);
        String filename = entityOptional.get().getFilename();
        filename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentLength(fetch.length);
        response.setContentType("application/x-bittorrent");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        if (response.isCommitted()) return;
        response.getOutputStream().write(fetch);
        response.getOutputStream().flush();
    }

}
