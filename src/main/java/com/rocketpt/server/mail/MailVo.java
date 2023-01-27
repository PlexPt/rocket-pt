package com.rocketpt.server.mail;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author intent
 * @version 1.0
 * @date 2020/1/20 2:22 下午
 * @email zzy.main@gmail.com
 */
@Getter
@Setter
@ToString
public class MailVo {
    @Schema(name = "邮件id", example = "1", required = true)
    private String id;
    @Schema(name = "邮件发送人", example = "123456@qq.com", required = true)
    private String from;
    @Schema(name = "邮件接收人", example = "123456@qq.com", required = true)
    private String to;
    @Schema(name = "邮件主题", example = "邀请码", required = true)
    private String subject;
    @Schema(name = "邮件内容", example = "邀请码", required = true)
    private String text;
    @Schema(name = "发送时间", required = true)
    private Date sentDate;
    @Schema(name = "抄送")
    private String cc;
    @Schema(name = "密送")
    private String bcc;
    @Schema(name = "状态")
    private String status;
    @Schema(name = "报错信息", example = "xxxx")
    private String error;

    @Schema(name = "邮件附件")
    @JsonIgnore
    private MultipartFile[] multipartFiles;
}
