package com.rocketpt.server.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.PageUtil;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.dto.entity.SysConfigEntity;
import com.rocketpt.server.dto.param.KeywordPageParam;
import com.rocketpt.server.service.sys.SystemConfigService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统配置信息
 *
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-03-27 20:53:32
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name = "系统配置", description = Constants.FinishStatus.FINISHED)
@RequestMapping("/sys/config")
public class SysConfigController {

    final SystemConfigService service;

    /**
     * 列表
     */
    @Operation(summary = "系统配置列表")
    @RequestMapping("/list")
    @SaCheckPermission("sys:config:view")
    public Result list(@RequestBody KeywordPageParam param) {
        String keyword = param.getKeyword();
        final boolean search = StringUtils.isNotBlank(keyword);

        PageUtil.startPage(param);
        List<SysConfigEntity> list = service.list(new QueryWrapper<SysConfigEntity>()
                .lambda()
                .and(search, w -> w
                        .like(search, SysConfigEntity::getK, keyword)
                        .or()
                        .like(search, SysConfigEntity::getV, keyword)
                )
                .orderByDesc(SysConfigEntity::getUpdateTime));

        return Result.ok(list, PageUtil.getPage(list));
    }


    /**
     * 信息
     */
    @Operation(summary = "系统配置详情")
    @SaCheckPermission("sys:config:view")
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id) {
        SysConfigEntity entity = service.getById(id);

        return Result.ok(entity);
    }

    /**
     * 保存/修改
     */
    @Operation(summary = "保存/修改")
    @SaCheckPermission("sys:config:edit")
    @RequestMapping("/save")
    public Result saveOrUpdate(@RequestBody SysConfigEntity entity) {

        service.saveOrUpdate(entity);

        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @SaCheckPermission("sys:config:edit")
    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids) {
        service.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
