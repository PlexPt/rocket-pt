package com.rocketpt.server.service;

import com.alibaba.fastjson.JSON;
import com.rocketpt.server.dao.UserDao;
import com.rocketpt.server.dto.entity.TorrentEntity;
import com.rocketpt.server.dto.entity.TorrentPeerEntity;
import com.rocketpt.server.dto.entity.UserEntity;
import com.rocketpt.server.dto.param.AnnounceRequest;
import com.rocketpt.server.dto.vo.TrackerResponse;
import com.rocketpt.server.service.sys.UserService;
import com.rocketpt.server.service.validator.ValidationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnnounceService {

    final UserDao userDao;
    final UserService userService;

    final TorrentPeerService torrentPeerService;


    final ValidationManager validationManager;

    public Map<String, Object> announce(AnnounceRequest request) {

        validationManager.validate(request);

        //TODO 处理短时间内重复的请求

        TorrentEntity torrent = request.getTorrent();

        //省略peer id
        boolean noPeerId = request.isNoPeerId();

        List<Map<String, Object>> peerList = getPeerList(request, 200);

        updatePeer(request, null);

        updateUserInfo(request);
        // 返回peer列表给客户端
        //TODO 默认值是60，改为动态调整
        Integer interval = getAnnounceInterval(request);
        TrackerResponse trackerResponse = TrackerResponse.build(interval, 60, torrent.getSeeders(), torrent.getLeechers(), peerList);
        return trackerResponse.toResultMap();
    }

    private void updateUserInfo(AnnounceRequest request) {
        UserEntity user = request.getUser();
        TorrentEntity torrent = request.getTorrent();
        Integer userId = request.getUser().getId();
        Integer torrentId = request.getTorrent().getId();

        TorrentPeerEntity peer = torrentPeerService.getPeer(userId, torrentId, request.getPeerId());
        if (peer == null) {
            //TODO
            peer = tryInsertOrUpdatePeer(request);
        }

        long lastUploaded = peer.getUploaded();
        long lastDownload = peer.getDownloaded();
        long uploadedOffset = request.getUploaded() - lastUploaded;
        long downloadedOffset = request.getDownloaded() - lastDownload;

        LocalDateTime updateTime = torrent.getUpdateTime();
        if (uploadedOffset < 0) {
            uploadedOffset = request.getUploaded();
        }
        if (downloadedOffset < 0) {
            downloadedOffset = request.getDownloaded();
        }

        user.setRealDownloaded(user.getRealDownloaded() + lastDownload);
        user.setRealUploaded(user.getRealUploaded() + lastUploaded);

        //TODO 优惠
        user.setUploaded(user.getUploaded() + uploadedOffset);
        user.setDownloaded(user.getDownloaded() + downloadedOffset);
        user.setSeedtime(user.getSeedtime() + (Instant.now().toEpochMilli() - updateTime.toInstant(ZoneOffset.UTC).toEpochMilli()));
        userService.updateById(user);

        //TODO 最后处理删除
        //TODO 校正BEP标准的计算方式


    }

    public void updatePeer(AnnounceRequest request, TorrentPeerEntity peerSelf) {
        String event = StringUtils.trimToEmpty(request.getEvent());
        log.info("Peer event {}: {}", event, JSON.toJSONString(request, true));
        Integer userId = request.getUser().getId();
        Integer torrentId = request.getTorrent().getId();
        //TODO 加入分布式锁

        // 任务停止 删除peer
        if (AnnounceRequest.EventType.stopped.equalsIgnoreCase(event)) {

            // 只有当有peer存在的时候才执行删除操作
            if (torrentPeerService.peerExists(userId, torrentId, request.getPeerId())) {
                torrentPeerService.delete(userId, torrentId, request.getPeerIdHex());
            }

            return;
        } else if (AnnounceRequest.EventType.started.equalsIgnoreCase(event)) {
            tryInsertOrUpdatePeer(request);

        } else if (AnnounceRequest.EventType.completed.equalsIgnoreCase(event)) {
            torrentPeerService.delete(userId, torrentId, request.getPeerIdHex());
            tryInsertOrUpdatePeer(request);

        } else {
            torrentPeerService.delete(userId, torrentId, request.getPeerIdHex());
            tryInsertOrUpdatePeer(request);
        }
    }

    /**
     * 获取 peer 列表
     *
     * @param peerNumWant 这个参数表明你希望从方法返回多少个 peers。如果当前的系统中现有的 peer 数量小于你想要的 peerNumWant，那么就返回所有的
     *                    peers；否则，只返回你想要的 peerNumWant 数量的 peers。
     * @return
     */
    private List<Map<String, Object>> getPeerList(AnnounceRequest request, Integer peerNumWant) {

        Integer torrentId = request.getTorrent().getId();
        Boolean seeder = request.getSeeder();
        boolean noPeerId = request.isNoPeerId();
        Integer userId = request.getUser().getId();
        String peerIdHex = request.getPeerIdHex();

        //TODO 从数据库获取peer列表
        //TODO 根据 seeder peerNumWant 参数限制peer
        //如果当前用户是 seeder，那么这段代码将寻找 leecher；如果当前用户不是 seeder（或者不确定是否是 seeder），那么就不对 peer 的类型进行过滤。
        List<TorrentPeerEntity> list = torrentPeerService.listByTorrent(torrentId, seeder, peerNumWant);

        List<Map<String, Object>> result = list.stream()
                .map(peer -> {

                    // 当前 Peer 自己不返回
                    if (peer.getUserId().equals(userId) && peer.getPeerId().equalsIgnoreCase(peerIdHex)) {
                        return null;
                    }

                    Map<String, Object> dataMap = new HashMap<>();
                    // 处理ipv4
                    if (StringUtils.isNotBlank(peer.getIp())) {
                        dataMap.put("ip", peer.getIp());
                        dataMap.put("port", peer.getPort());
                        if (!noPeerId) {
                            dataMap.put("peer id", peer.getPeerId());
                        }
                    }
                    //TODO 支持ipv6
                    //TODO 支持压缩
                    return dataMap.isEmpty() ? null : dataMap;
                })
                .filter(peer -> peer != null)
                .collect(Collectors.toList());
        return result;
    }


    private TorrentPeerEntity tryInsertOrUpdatePeer(AnnounceRequest request) {
        try {

            TorrentPeerEntity peerEntity = new TorrentPeerEntity();
            peerEntity.setUserId(request.getUser().getId());
            peerEntity.setTorrentId(request.getTorrent().getId());
            peerEntity.setPeerId(request.getPeer_id());
            peerEntity.setPeerIdHex(request.getPeerIdHex());
            peerEntity.setPort(request.getPort());
            peerEntity.setDownloaded(request.getDownloaded());
            peerEntity.setUploaded(request.getUploaded());
            peerEntity.setRemaining(request.getLeft());
            peerEntity.setSeeder(request.getSeeder());
            peerEntity.setUserAgent(request.getUserAgent());
            peerEntity.setPasskey(request.getPasskey());
            peerEntity.setCreateTime(LocalDateTime.now());
            peerEntity.setLastAnnounce(LocalDateTime.now());

            peerEntity.setIp(request.getIp());
            peerEntity.setIpv6(request.getIpv6());

            if (StringUtils.isBlank(peerEntity.getIp())) {
                peerEntity.setIp(request.getRemoteAddr());
            }

            torrentPeerService.save(peerEntity);
            return peerEntity;

        } catch (Exception exception) {
            log.error("Peer update error update: ", exception);
        }
        return null;
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

        return 60;
    }
}
