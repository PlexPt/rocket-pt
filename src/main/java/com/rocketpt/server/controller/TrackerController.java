package com.rocketpt.server.controller;


import cn.hutool.core.util.HexUtil;
import com.rocketpt.server.dto.param.AnnounceRequest;
import com.rocketpt.server.service.AnnounceService;
import com.rocketpt.server.util.BencodeUtil;
import com.rocketpt.server.util.BinaryFieldUtil;
import com.rocketpt.server.util.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * <p>
     * 1. bep建议接口路径是 /announce
     * 2. 成功响应
     * 返回一个经过 Bencode 编码的 Dictionary （也就是 Map），包含以下 key：
     * <p>
     * interval – 代表间隔时间，单位是秒，表示 BT 客户端应该指定时间之后再与 Tracker 联系更新状态
     * peers – 这是个 List，每个 List 存储一个 Dictionary（也就是 Map），每个 Dictionary 包含以下 key：
     * peer id  客户端随机唯一 ID
     * ip 客户端 IP 地址，既可是 IPV4，也可以是 IPV6，以常规字符串表示即可，如 `127.0.0.11 或者 ::1，也支持 DNS 名称
     * port 客户端端口号
     */
    @GetMapping("/announce")
    public String announce(HttpServletRequest request,
                           @ModelAttribute AnnounceRequest announceRequest,
                           @RequestHeader(name = "User-Agent") String ua,
                           @RequestHeader(name = "want-digest", required = false) String wantDigest) {

        String queryStrings = request.getQueryString();
        //TODO 处理多IP
        log.info("收到announce汇报：" + queryStrings);
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
     * 一个特殊的请求，它允许用户获取关于一个或多个 torrent 的基本统计信息，而不需要完全加入 swarm（即 torrent 的下载/上传群体）。
     * 通常，这些信息包括：完成下载的次数（即有多少 peers 拥有了文件的全部内容）、正在下载的用户数量（leechers）、拥有完整文件并且正在分发的用户数量（seeders）。
     * 通过 scrape 请求，用户可以快速了解 torrent 的“健康状况”，而无需加入 swarm。例如，一个拥有很多 seeders 的 torrent 可能下载速度更快，表明它是一个活跃的 torrent。
     * 另一方面，如果一个 torrent 没有 seeders，那么新用户可能无法下载到完整的文件。
     * 但是，对于PT来说，scrape 请求对于一般用户而言并不是必需，用户可以从网站的页面上获得关于 torrent 健康状况的足够信息，无需直接进行 scrape 请求。
     * <p>
     * http://www.bittorrent.org/beps/bep_0048.html
     */
    @GetMapping("/scrape")
    public String processingScrape(Optional<String> passkey,
                                   HttpServletRequest request) {
        String queryString = request.getQueryString();
        //收到scrape汇报：passkey=xxx&info_hash=X0%BE%xxx7%A0
        log.info("收到scrape汇报：" + queryString);

        if (StringUtils.isEmpty(queryString)) {
            return BencodeUtil.error();
        }

        List<byte[]> infoHashesHex = BinaryFieldUtil.matchInfoHashesHex(queryString);

        return BencodeUtil.errorNoRetry("dont touch server");
    }
}
