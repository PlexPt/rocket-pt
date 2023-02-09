package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Res;
import com.rocketpt.server.dto.entity.TorrentsEntity;
import com.rocketpt.server.dto.param.TorrentParam;
import com.rocketpt.server.service.TorrentsService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Objects;

import io.swagger.v3.oas.annotations.tags.Tag;
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

}
