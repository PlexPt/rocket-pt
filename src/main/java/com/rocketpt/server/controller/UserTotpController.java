package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.dto.vo.TotpVo;
import com.rocketpt.server.service.GoogleAuthenticatorService;
import com.rocketpt.server.service.sys.UserCredentialService;
import com.rocketpt.server.service.sys.UserService;

import org.springframework.boot.info.BuildProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@RestController
@Tag(name = "2FA 两步验证相关", description = Constants.FinishStatus.FINISHED)
@RequiredArgsConstructor
@RequestMapping("/totp")
public class UserTotpController {

    private final UserService userService;

    private final UserCredentialService userCredentialService;
    private final GoogleAuthenticatorService googleAuthenticatorService;


    @Operation(summary = "生成2FA二维码", description = "生成2FA二维码")
    @ApiResponse(responseCode = "0", description = "操作成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation =
                    TotpVo.class))
    })
    @PostMapping("/code")
    public Result code() {
        String key = googleAuthenticatorService.createCredentials();
        String username = userService.getUsername(userService.getUserId());
        //TODO 站名自定义
        String uri = GoogleAuthenticatorService.generateTotpUri(key, username, "rocketpt");

        TotpVo totpVo = new TotpVo();
        totpVo.setKey(key);
        totpVo.setUri(uri);
        return Result.ok(totpVo);
    }


    @Operation(summary = "验证保存2FA", description = "这个接口接收用户输入的两步认证码，并验证它是否正确，正确则保存")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated TotpVo vo) {

        // 获取用户的TOTP
        String totp = vo.getKey();

        // 验证TOTP码是否有效
        boolean codeValid = userService.isTotpValid(vo.getCode(), totp);
        if (!codeValid) {
            return Result.error("验证码不正确");
        }
        userCredentialService.updateTotp(userService.getUserId(), totp);
        return Result.ok();
    }

    @Operation(summary = "删除2FA")
    @PostMapping("/remove")
    public Result remove() {
        userCredentialService.removeTotp(userService.getUserId());
        return Result.ok();
    }

}
