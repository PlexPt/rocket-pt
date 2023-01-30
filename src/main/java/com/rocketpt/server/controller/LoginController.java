package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.controller.param.LoginParam;
import com.rocketpt.server.sys.dto.UserinfoDTO;
import com.rocketpt.server.sys.service.CaptchaService;
import com.rocketpt.server.sys.service.SessionService;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final SessionService sessionService;
    private final CaptchaService captchaService;


    /**
     * 验证码
     */
    @GetMapping("code.jpg")
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
    private ResponseEntity<UserinfoDTO> login(@RequestBody LoginParam param) {
        if (!captchaService.verifyCaptcha(param.uuid(), param.code())) {
            throw new RocketPTException("验证码不正确");
        }

        return ResponseEntity.ok(sessionService.login(param.username(), param.password()));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER_NAME).replace("Bearer", "").trim();
        sessionService.logout(token);
        return ResponseEntity.ok().build();
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/userinfo")
    public ResponseEntity<UserinfoDTO> userInfo(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER_NAME).replace("Bearer", "").trim();
        return ResponseEntity.ok(sessionService.getLoginUserInfo(token));
    }


}
