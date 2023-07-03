package com.rocketpt.server.service;

import com.rocketpt.server.dao.UserDao;
import com.rocketpt.server.dto.entity.TorrentEntity;
import com.rocketpt.server.dto.param.AnnounceRequest;
import com.rocketpt.server.service.validator.ValidationManager;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnnounceService {

    final UserDao userDao;

    final TorrentPeerService torrentPeerService;


    final ValidationManager validationManager;

    public Map<String, Object> announce(AnnounceRequest request) {

        validationManager.validate(request);

        // 获取peer列表
        torrentPeerService.list();

        TorrentEntity torrent = request.getTorrent();

        Integer interval = getAnnounceInterval(request);
        List peerList = getPeerList();

        // 返回peer列表给客户端
        return getMap(interval, 60, torrent.getSeeders(), torrent.getLeechers(), peerList);
    }

    private List getPeerList() {
        //TODO 从数据库获取peer列表
        return new ArrayList();
    }

    /**
     * @return
     */
    public Map<String, Object> getMap(Integer interval, Integer minInterval, Integer complete,
                                      Integer incomplete, List peers) {

        return Map.of(
                "interval", interval,
                "min interval", 60,
                "complete", complete,
                "incomplete", incomplete,
                "peers", peers
        );
    }

    /**
     * 广播间隔
     * 策略：
     * 基于种子发布的时间长度来调整广播间隔：类似NP
     * 如 种子发布7天内，广播间隔为 600s
     * 种子发布大于7天，小于30天， 广播间隔1800s
     * 种子发布30天以上，广播间隔3600s
     * <p>
     * <p>
     * 基于活跃度调整广播间隔：你可以根据种子的活跃度（例如活跃peer的数量或者下载/上传速度）来调整广播间隔。
     * 如果一个种子的活跃度高，说明它需要更频繁地更新和广播peer列表。
     * 如果活跃度低，可以增加广播间隔以减少服务器负载。
     * <p>
     * 基于服务器负载调整广播间隔：如果你的服务器负载高（例如CPU
     * 使用率高，内存使用量高，或者网络带宽使用高），可以增加广播间隔以减少负载。
     * 如果服务器负载低，可以减小广播间隔以提高文件分享的效率。
     * <p>
     * 动态调整广播间隔：你可以实时监控你的网络状况、服务器状况、以及种子的活跃度，然后动态调整广播间隔。
     * 例如，如果你发现某个时间段用户数量增多，可以临时减小广播间隔。如果发现某个时间段用户数量减少，可以增加广播间隔。
     * <p>
     * <p>
     * 综合策略：
     * <p>
     * 种子发布7天内或活跃peer数大于1000，广播间隔为 600s
     * 种子发布大于7天，小于30天或活跃peer数在100-1000之间，广播间隔为 1800s
     * 种子发布30天以上或活跃peer数小于100，广播间隔为 3600s
     *
     * @param request
     * @return
     */
    private Integer getAnnounceInterval(AnnounceRequest request) {
        //TODO 广播间隔

        return 600;
    }
}
