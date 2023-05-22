package com.rocketpt.server.service.mail;


/**
 * @author intent
 * @version 1.0
 * @date 2020/1/20 2:17 下午
 * @email zzy.main@gmail.com
 */
public interface MailService {
    MailVo sendMail(MailVo mailVo);

    MailVo sendMail(String email, String subject, String text, String url);
}
