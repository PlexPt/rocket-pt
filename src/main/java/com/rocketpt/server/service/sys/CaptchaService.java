package com.rocketpt.server.service.sys;


import java.awt.image.BufferedImage;

public interface CaptchaService {
    /**
     * 生成验证码
     */
    String create(String uuid);

    /**
     * 生成图片
     *
     * @param num
     * @return
     */
    BufferedImage getCaptcha(String num);

    /**
     * 存储验证码
     *
     * @param id
     * @param captcha 验证码
     */
    void saveCaptcha(String id, String captcha);

    /**
     * 移除验证码
     *
     * @param id id
     * @return remove true
     */
    boolean removeCaptcha(String id);

    /**
     * 验证验证码是否正确 验证完就过期
     *
     * @param id      id
     * @param captcha 输入的验证码
     * @return true则正确
     */
    boolean verifyCaptcha(String id, String captcha);
}
