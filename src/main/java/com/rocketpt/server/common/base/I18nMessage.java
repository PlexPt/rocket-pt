package com.rocketpt.server.common.base;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Objects;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class I18nMessage {

    static {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(5);
        messageSource.setBasenames("classpath:i18n/message");
        I18nMessage.init(messageSource);
    }

    private static MessageSource messageSource;

    public static void init(MessageSource messageSource) {
        Objects.requireNonNull(messageSource, "MessageSource can't be null");
        I18nMessage.messageSource = messageSource;
    }

    /**
     * 读取国际化消息
     *
     * @param msgCode 消息码
     * @param args    消息参数 例: new String[]{"1","2","3"}
     * @return
     */
    public static String getMessage(String msgCode, Object[] args) {
        try {
            return I18nMessage.messageSource.getMessage(msgCode, args,
                    LocaleContextHolder.getLocale());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
            log.error("===> 读取国际化消息失败, code:{}, args:{}, ex:{}", msgCode, args,
                    e.getMessage() == null ? e.toString() : e.getMessage());
        }
        return "-Unknown-";
    }

    /**
     * 获取一条语言配置信息
     *
     * @param msgCode 消息码
     * @return 对应配置的信息
     */
    public static String getMessage(String msgCode) {
        return I18nMessage.getMessage(msgCode, null);
    }
}
