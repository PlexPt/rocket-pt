package com.rocketpt.server.service.mail;

import com.rocketpt.server.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 邮件
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    final JavaMailSenderImpl mailSender;//注入邮件工具类

    final SysConfigService configService;

    @Async
    @Override
    public MailVo sendMail(MailVo mailVo) {
        try {
            checkMail(mailVo); //1.检测邮件
            sendMimeMail(mailVo); //2.发送邮件
            return saveMail(mailVo); //3.保存邮件
        } catch (Exception e) {
            e.printStackTrace();
            mailVo.setStatus("fail");
            mailVo.setError(e.getMessage());
            return mailVo;
        }
    }


    @Override
    public MailVo sendMail(String email, String subject, String text, String url) {
        MailVo mailVo = new MailVo();
        mailVo.setFrom(mailSender.getUsername());
        mailVo.setTo(email);
        mailVo.setSubject(subject);
        mailVo.setText(text.replaceAll("\\?url", url));
        return sendMail(mailVo);
    }

    //检测邮件信息类
    private void checkMail(MailVo mailVo) {
        if (StringUtils.isEmpty(mailVo.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getFrom())) {
            throw new RuntimeException("发件人不能为空");
        }
    }

    //构建复杂邮件信息类
    private void sendMimeMail(MailVo mailVo) {
        try {
            //true表示支持复杂类型
            MimeMessageHelper messageHelper =
                    new MimeMessageHelper(mailSender.createMimeMessage(), true);
            if (StringUtils.isEmpty(mailVo.getFrom())) {
                // 邮件发信人从配置项读取
                messageHelper.setFrom(getMailSendFrom());
            } else {
                messageHelper.setFrom(mailVo.getFrom());
            }
            //邮件发信人
            messageHelper.setTo(mailVo.getTo().split(","));
            //邮件收信人
            messageHelper.setSubject(mailVo.getSubject());
            //邮件主题
            messageHelper.setText(mailVo.getText(), true);
            //邮件内容
            if (!StringUtils.isEmpty(mailVo.getCc())) {
                //抄送
                messageHelper.setCc(mailVo.getCc().split(","));
            }
            if (!StringUtils.isEmpty(mailVo.getBcc())) {
                //密送
                messageHelper.setCc(mailVo.getBcc().split(","));
            }
            if (mailVo.getMultipartFiles() != null) {
                //添加邮件附件
                for (MultipartFile multipartFile : mailVo.getMultipartFiles()) {
                    messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }
            if (mailVo.getSentDate() == null) {
                //发送时间
                mailVo.setSentDate(new Date());
                messageHelper.setSentDate(mailVo.getSentDate());
            }
            mailSender.send(messageHelper.getMimeMessage());
            //正式发送邮件
            mailVo.setStatus("ok");
        } catch (Exception e) {
            //发送失败
            throw new RuntimeException(e);
        }
    }

    //保存邮件
    private MailVo saveMail(MailVo mailVo) {
        //将邮件保存到数据库..
        return mailVo;
    }

    //获取邮件发信人
    public String getMailSendFrom() {
        return mailSender.getJavaMailProperties().getProperty("from");
    }
}
