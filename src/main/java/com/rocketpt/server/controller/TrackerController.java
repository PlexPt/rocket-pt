package com.rocketpt.server.controller;


import com.rocketpt.server.dto.param.AnnounceRequest;
import com.rocketpt.server.service.AnnounceService;
import com.rocketpt.server.util.BencodeUtil;
import com.rocketpt.server.util.BinaryFieldUtil;
import com.rocketpt.server.util.IPUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cn.hutool.core.util.HexUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tracker")
public class TrackerController {
    final AnnounceService announceService;

    /**
     * http://www.bittorrent.org/beps/bep_0003.html
     * <p>
     * want-digest 是为了校验aria2
     */
    @GetMapping("/announce")
    public String announce(HttpServletRequest request,
                           @ModelAttribute AnnounceRequest announceRequest,
                           @RequestHeader(name = "User-Agent") String ua,
                           @RequestHeader(name = "want-digest", required = false) String wantDigest) {

        String queryStrings = request.getQueryString();
        String ipAddr = IPUtils.getIpAddr();
        byte[] peerId = BinaryFieldUtil.matchPeerId(queryStrings);

        String peerIdHex = HexUtil.encodeHexStr(peerId);

        announceRequest.setSeeder(announceRequest.getLeft().equals(0L));
        announceRequest.setInfoHash(BinaryFieldUtil.matchInfoHash(queryStrings));
        announceRequest.setPeerId(peerId);
        announceRequest.setPeerIdHex(peerIdHex);
        announceRequest.setRemoteAddr(ipAddr);
        announceRequest.setWantDigest(wantDigest);
        announceRequest.setUserAgent(ua);

        Map<String, Object> response = announceService.announce(announceRequest);

        String responseStr = BencodeUtil.encode(response);
        return responseStr;

    }

    /**
     * 从请求中获取所有info_hash信息，
     * 从数据库中匹配出来对应的做种内容，
     * <p>
     * http://www.bittorrent.org/beps/bep_0048.html
     */
    @GetMapping("/scrape")
    public String processingScrape(Optional<String> passkey,
                                   HttpServletRequest request) {
        var q = request.getQueryString();
        if (StringUtils.isEmpty(q)) {
            return BencodeUtil.error();
        }


        List<byte[]> infoHashesHex = BinaryFieldUtil.matchInfoHashesHex(q);

        Map<String, Object> result = new HashMap<>();


        return BencodeUtil.encode(result);
    }
}
