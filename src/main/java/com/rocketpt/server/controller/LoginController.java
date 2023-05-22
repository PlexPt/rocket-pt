package com.rocketpt.server.controller;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dto.param.ChangePasswordParam;
import com.rocketpt.server.dto.param.ForgotPasswordParam;
import com.rocketpt.server.dto.param.LoginParam;
import com.rocketpt.server.dto.param.ResetPasswordParam;
import com.rocketpt.server.dto.sys.UserinfoDTO;
import com.rocketpt.server.service.sys.CaptchaService;
import com.rocketpt.server.service.sys.UserService;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author plexpt
 */
@RestController
@Tag(name = "登录相关", description = Constants.FinishStatus.FINISHED)
@RequiredArgsConstructor
public class LoginController {

    private final CaptchaService captchaService;

    private final UserService userService;


    /**
     * 验证码
     */
    @Operation(summary = "获取验证码", description = "根据uuid获取验证码")
    @Parameter(name = "uuid", description = "前端随机生成的UUID", required = true, in = ParameterIn.QUERY)
    @GetMapping("/code.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        String code = captchaService.create(uuid);
        //获取图片验证码
        BufferedImage image = captchaService.getCaptcha(code);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }


    @Operation(summary = "登录", description = "根据 用户名密码登录")
    @PostMapping("/login")
    private Result login(@Validated @RequestBody LoginParam param) {
        if (!captchaService.verifyCaptcha(param.getUuid(), param.getCode())) {
            throw new RocketPTException("验证码不正确");
        }

        Long userId = userService.login(param);
        if (userId > 0) {
            StpUtil.login(userId);

            return Result.ok();
        }

        throw new UserException(CommonResultStatus.UNAUTHORIZED, "密码不正确");
    }


    @Operation(summary = "忘记密码", description = "忘记密码 1 发送验证码 2 发送邮件 3 根据邮件重置密码")
    @PostMapping("/forgot-password")
    private Result forgotPassword(@Validated @RequestBody ForgotPasswordParam param) {
        if (!captchaService.verifyCaptcha(param.getUuid(), param.getCode())) {
            throw new RocketPTException("验证码不正确");
        }

        userService.forgotPassword(param);

        return Result.ok();

    }


    @Operation(summary = "重置密码", description = "根据邮件重置密码")
    @PostMapping("/reset-password")
    private Result resetPassword(@Validated @RequestBody ResetPasswordParam param) {
        if (!captchaService.verifyCaptcha(param.getUuid(), param.getCode())) {
            throw new RocketPTException("验证码不正确");
        }

        userService.resetPassword(param);

        return Result.ok();
    }

    @Operation(summary = "修改密码", description = "根据旧密码改密码")
    @PostMapping("/change-password")
    private Result changePassword(@Validated @RequestBody ChangePasswordParam param) {

        userService.changePassword(param);

        return Result.ok();
    }

    @SaIgnore
    @Operation(summary = "是否登录")
    @GetMapping("isLogin")
    public Result isLogin() {
        return Result.ok(StpUtil.isLogin());
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    @SneakyThrows
    public Result logout() {
        // 当前会话注销登录
        StpUtil.logout();
        return Result.ok("成功");
    }

    @Operation(summary = "用户信息")
    @SaCheckLogin
    @GetMapping("/userinfo")
    public Result info() {
        UserinfoDTO userInfo = userService.getUserInfo();

        return Result.ok(userInfo);
    }


}
