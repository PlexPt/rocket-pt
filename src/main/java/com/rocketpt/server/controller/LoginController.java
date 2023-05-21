package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.dto.param.LoginParam;
import com.rocketpt.server.sys.service.CaptchaService;
import com.rocketpt.server.sys.service.SessionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author plexpt
 */
@RestController
@Tag(name = "登录相关", description = Constants.FinishStatus.FINISHED)
@RequiredArgsConstructor
public class LoginController {

    private final SessionService sessionService;
    private final CaptchaService captchaService;


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
        if (!captchaService.verifyCaptcha(param.uuid(), param.code())) {
            throw new RocketPTException("验证码不正确");
        }

        return Result.ok(sessionService.login(param.username(), param.password()));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER_NAME).replace("Bearer", "").trim();
        sessionService.logout(token);
        return Result.ok();
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/userinfo")
    public Result userInfo(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER_NAME).replace("Bearer", "").trim();
        return Result.ok(sessionService.getLoginUserInfo(token));
    }

}
