package com.rocketpt.server.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取请求主机IP地址工具
 *
 * @author pt
 */
@Slf4j
public class IPUtils {

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     * <p>
     * x-real-ip:171.111
     * x-forwarded-for:171.1.157.11
     * remote-host:171.111.157.11
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr() {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();


        //打印头信息
//        Enumeration enums = request.getHeaderNames();
//        while (enums.hasMoreElements()) {
//            String headerName = (String) enums.nextElement();
//            String headerValue = request.getHeader(headerName);
//            log.info(headerName + ":" + headerValue);
//        }


        return getIpAddr(request);
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     * <p>
     * x-real-ip:171.111
     * x-forwarded-for:171.1.157.11
     * remote-host:171.111.157.11
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {

        String ip = null;
        try {
            ip = request.getHeader("x-real-ip");


            if (isUnknownIp(ip)) {
                ip = request.getHeader("x-forwarded-for");

            }
            if (isUnknownIp(ip)) {
                ip = request.getHeader("remote-host");
            }
            if (isUnknownIp(ip)) {
                ip = request.getHeader("Proxy-Client-IP");

            }
            if (isUnknownIp(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (isUnknownIp(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }

        //使用代理，则获取第一个IP地址
        if (StringUtils.isNotBlank(ip) && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }

        return ip;
    }


    private static boolean isUnknownIp(String ip) {
        return StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip);
    }

}
