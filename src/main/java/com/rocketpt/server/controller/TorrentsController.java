package com.rocketpt.server.controller;

import com.rocketpt.server.common.base.Res;
import com.rocketpt.server.web.entity.TorrentsEntity;
import com.rocketpt.server.web.entity.param.TorrentParam;
import com.rocketpt.server.web.service.TorrentsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import lombok.RequiredArgsConstructor;


/**
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-01-28 00:01:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/torrent")
public class TorrentsController {

    private final TorrentsService torrentsService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Res list(@RequestBody TorrentParam params) {
        return torrentsService.queryPage(params);
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
    public Res upload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            byte[] bytes = multipartFile.getBytes();
            return torrentsService.upload(bytes);
        } catch (IOException e) {
            return Res.failure();
        }
    }

}
