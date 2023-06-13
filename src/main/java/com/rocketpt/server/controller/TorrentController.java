package com.rocketpt.server.controller;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.base.PageUtil;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dao.SuggestDao;
import com.rocketpt.server.dto.entity.TorrentEntity;
import com.rocketpt.server.dto.param.TorrentAddParam;
import com.rocketpt.server.dto.param.TorrentAuditParam;
import com.rocketpt.server.dto.param.TorrentParam;
import com.rocketpt.server.dto.vo.SuggestVo;
import com.rocketpt.server.dto.vo.TorrentVO;
import com.rocketpt.server.service.TorrentService;
import com.rocketpt.server.service.sys.UserService;

import org.springframework.util.StringUtils;
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
import java.util.ArrayList;
import java.util.List;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;


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
public class TorrentController {

    private final TorrentService torrentService;

    private final SuggestDao suggestDao;
    private final UserService userService;

    /**
     * 种子列表查询
     */
    @SaCheckLogin
    @Operation(summary = "种子列表查询", description = "种子列表条件查询-分页-排序")
    @ApiResponse(responseCode = "0", description = "操作成功",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TorrentVO.class))
            })
    @PostMapping("/list")
    public Result list(@RequestBody TorrentParam param) {
        param.validOrder(param.getOrderKey(TorrentEntity.class));
        param.buildLike();
        PageUtil.startPage(param);

        List<TorrentEntity> list = torrentService.getBaseMapper().search(param);

        return Result.ok(list, PageUtil.getPage(list));
    }

    @Operation(summary = "种子搜索建议")
    @GetMapping("/suggest")
    @Parameter(name = "q", description = "关键字", required = true, in = ParameterIn.QUERY)
    @ApiResponse(responseCode = "0", description = "操作成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation =
                    SuggestVo.class))
    })
    public Result getSuggestions(@RequestParam(required = false) String q) {

        if (StringUtils.isEmpty(q)) {
            return Result.ok(new ArrayList<>());
        }

        List<SuggestVo> suggests = suggestDao.getSuggestions(q.trim() + "%");
        List<SuggestVo> result = new ArrayList<>();
        int i = 0;
        for (SuggestVo suggest : suggests) {
            if (suggest.getSuggest().length() > 25) {
                //TODO 保存的时候过滤
                continue;
            }
            result.add(suggest);

            i++;
            if (i >= 5) {
                break;
            }
        }

        return Result.ok(result);
    }

    @SaCheckLogin
    @Operation(summary = "种子详情查询")
    @ApiResponse(responseCode = "0", description = "操作成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation =
                    TorrentEntity.class))
    })
    @PostMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {

        TorrentEntity entity = torrentService.getById(id);
        return Result.ok(entity);
    }


    @SaCheckLogin
    @Operation(summary = "新增种子")
    @PostMapping("/add")
    public Result add(@RequestBody @Validated TorrentAddParam param) {

        torrentService.add(param);

        return Result.ok();
    }


    @SaCheckLogin
    @Operation(summary = "审核种子")
    @PostMapping("/audit")
    public Result audit(@RequestBody @Validated TorrentAuditParam param) {

        torrentService.audit(param);

        return Result.ok();
    }

    /**
     * 收藏或者取消收藏
     */
    @Operation(summary = "收藏或者取消收藏种子")
    @Parameter(name = "id", description = "种子ID", required = true, in = ParameterIn.QUERY)
    @PostMapping("/favorite")
    public Result favorite(Integer id) {
        torrentService.favorite(id, userService.getUserId());

        return Result.ok();
    }


    @SaCheckLogin
    @Operation(summary = "上传种子文件")
    @Parameter(name = "id", description = "种子ID", required = true, in = ParameterIn.QUERY)
    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile file,
                         @RequestParam Integer id) {
        try {
            if (file.isEmpty()) {
                throw new RocketPTException(CommonResultStatus.PARAM_ERROR,
                        I18nMessage.getMessage("torrent_empty"));
            }
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            // validation here for .torrent file
            if (!filename.endsWith(".torrent")) {
                throw new RocketPTException("Invalid file type. Only .torrent files are allowed");
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new RocketPTException("Cannot store file with relative path outside current" +
                        " directory: " + filename);
            }

            byte[] bytes = file.getBytes();

            torrentService.upload(id, bytes, filename);
        } catch (IOException e) {
            return Result.failure();
        }

        return Result.ok();
    }


    @Operation(summary = "修改种子")
    @PostMapping("/update")
    @SaCheckLogin
    public Result update(@RequestBody TorrentEntity entity) {
        torrentService.update(entity);

        return Result.ok();
    }


    @SaCheckLogin
    @Operation(summary = "删除种子")
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids) {
        torrentService.remove(ids);

        return Result.ok();
    }


    @SaIgnore
    @SneakyThrows
    @Operation(summary = "下载种子")
    @Parameter(name = "id", description = "种子ID", required = true, in = ParameterIn.QUERY)
    @Parameter(name = "passkey", description = "passkey", in = ParameterIn.QUERY)
    @GetMapping("/download")
    public void download(@RequestParam("id") @Positive Integer id,
                         @RequestParam(value = "passkey", required = false) @Positive String passkey,
                         HttpServletResponse response) {

        TorrentEntity entity = torrentService.getById(id);
        if (entity == null) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, I18nMessage.getMessage(
                    "torrent_not_exists"));
        }
        byte[] torrentBytes = torrentService.fetch(id, passkey);
        //TODO 下载日志
        //TODO 修改下载的文件名
        String filename = entity.getFilename();
        filename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentLength(torrentBytes.length);
        response.setContentType("application/x-bittorrent");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        if (response.isCommitted()) {
            return;
        }
        response.getOutputStream().write(torrentBytes);
        response.getOutputStream().flush();
    }

}
