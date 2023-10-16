package com.rocketpt.server.service;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.service.sys.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 系统配置
 *
 * @author plexpt
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TrackerURLService {

    final SysConfigService sysConfigService;


    /**
     * 获取当前的tracker Announce 地址
     */
    public String getAnnounce(String passkey) {

        String string = Constants.Announce.PROTOCOL + "://" + Constants.Announce.HOSTNAME + ":" + Constants.Announce.PORT + "/api/tracker/announce?passkey=" + passkey;

        return string;
    }
}

