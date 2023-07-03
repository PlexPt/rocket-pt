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
     *
     * 1. bep建议接口路径是 /announce
     * 2. 成功响应
     * 返回一个经过 Bencode 编码的 Dictionary （也就是 Map），包含以下 key：
     *
     * interval – 代表间隔时间，单位是秒，表示 BT 客户端应该指定时间之后再与 Tracker 联系更新状态
     * peers – 这是个 List，每个 List 存储一个 Dictionary（也就是 Map），每个 Dictionary 包含以下 key：
     * peer id 对等方客户端随机唯一 ID
     * ip 对等方 IP 地址，既可是 IPV4，也可以是 IPV6，以常规字符串表示即可，如 `127.0.0.11 或者 ::1，也支持 DNS 名称
     * port 对等方端口号
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
