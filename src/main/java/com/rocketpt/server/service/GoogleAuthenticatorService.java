package com.rocketpt.server.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import cn.hutool.crypto.SecureUtil;

/**
 * 生成密钥
 * 当用户启用两步验证时，调用 createCredentials() 方法生成密钥。将生成的密钥与用户账户关联，并展示给用户一个二维码，以便他们可以使用 Google
 * Authenticator 扫描。
 * <p>
 * 验证
 * 在用户尝试登录时，要求用户提供从 Google Authenticator 生成的动态验证码。然后调用 isCodeValid() 方法，传入密钥和用户提供的验证码，以验证其有效性。
 * <p>
 * 应用
 * 根据你的项目需求，将以上代码片段集成到用户注册、登录、验证等流程中。
 * <p>
 * 注意：谷歌两步验证器仅提供额外的安全层，不应替代其他安全措施，如密码强度检查、安全传输、数据加密等。
 *
 * @link https://github.com/wstrange/GoogleAuth
 */
@Service
public class GoogleAuthenticatorService {

    private final GoogleAuthenticator googleAuthenticator;

    public GoogleAuthenticatorService() {
        GoogleAuthenticatorConfig config =
                new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                        .build();
        this.googleAuthenticator = new GoogleAuthenticator(config);
    }

    public String createCredentials() {
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        return key.getKey();
    }

    public boolean isCodeValid(String secret, int verificationCode) {
        return googleAuthenticator.authorize(secret, verificationCode);
    }

    public static String generateTotpUri(String secretKey, String username, String issuer) {
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6" +
                        "&period=30",
                URLEncoder.encode(issuer, StandardCharsets.UTF_8),
                URLEncoder.encode(username, StandardCharsets.UTF_8),
                secretKey,
                URLEncoder.encode(issuer, StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {
        GoogleAuthenticatorService service = new GoogleAuthenticatorService();
        String secret = service.createCredentials();
        System.out.println(secret);
        String uri = generateTotpUri("F4PMZXL4GN5113TJGD6VLIEEK1P2427Z", "plexpt", "rocketpt");
        System.out.println(uri);

        String pass = "rocketpt";
        System.out.println(SecureUtil.sha256(pass));
    }
}
