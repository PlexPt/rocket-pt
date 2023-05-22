package com.rocketpt.server.controller;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dto.param.LoginParam;
import com.rocketpt.server.dto.sys.UserinfoDTO;
import com.rocketpt.server.service.sys.CaptchaService;
import com.rocketpt.server.service.sys.UserService;

import org.apache.tomcat.util.http.fileupload.IOUtils;
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


    @PostMapping("/login")
    private Result login(@RequestBody LoginParam param) {
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

    @SaIgnore
    @GetMapping("isLogin")
    public Result isLogin() {
        return Result.ok(StpUtil.isLogin());
    }

    @PostMapping("/logout")
    @SneakyThrows
    public Result logout() {
        // 当前会话注销登录
        StpUtil.logout();
        return Result.ok("成功");
    }

    @SaCheckLogin
    @GetMapping("/userinfo")
    public Result info() {
        UserinfoDTO userInfo = userService.getUserInfo();

        return Result.ok(userInfo);
    }


}
