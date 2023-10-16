package com.rocketpt.server.dto.param;

import com.rocketpt.server.dto.entity.TorrentEntity;
import com.rocketpt.server.dto.entity.UserEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Announce请求参数
 * <p>
 * 参考
 * http://bittorrent.org/beps/bep_0003.html
 * https://wiki.theory.org/BitTorrentSpecification
 * https://wiki.theory.org/BitTorrent_Tracker_Protocol
 */
@Data
@Slf4j
public class AnnounceRequest {
    //来自客户端的请求如下
    // qBittorrent v4.5.5
    //{
    //  "passkey": "xxxxxxxx",
    //  "info_hash": "%5d%df%19%a2%85%03",
    //  "peer_id": "-qB4550-NJ4pxxxkx)za",
    //  "port": 14042,
    //  "uploaded": 0,
    //  "downloaded": 0,
    //  "left": 135621375,
    //  "corrupt": 0,
    //  "key": "B99DE91E",
    //  "event": "started",
    //  "numwant": 200,
    //  "compact": 1,
    //  "no_peer_id": 1,
    //  "supportcrypto": 1,
    //  "redundant": 0,
    //  "ipv6": [
    //    "1111:222",
    //    "1111:333"
    //  ]
    //}


    //Transmission 4.0.3
    //{
    //  "passkey": "xxxxx",
    //  "info_hash": "xxx",
    //  "peer_id": "-TR4030-a2cdxxxxf42m",
    //  "port": 51413,
    //  "uploaded": 0,
    //  "downloaded": 0,
    //  "left": 135621375,
    //  "numwant": 80,
    //  "key": 2116472635,
    //  "compact": 1,
    //  "supportcrypto": 1,
    //  "event": "started"
    //}

    //uTorrent 3.6.0
    //{
    //  "passkey": "xxxxx",
    //  "info_hash": "xxx",
    //  "peer_id": "-UT360S-%24%b7%c1%5eAct%e6sy%2f%e9",
    //  "port": 16623,
    //  "uploaded": 0,
    //  "downloaded": 0,
    //  "left": 135621375,
    //  "corrupt": 0,
    //  "key": "D39D733C",
    //  "event": "started",
    //  "numwant": 200,
    //  "compact": 1,
    //  "no_peer_id": 1
    //}

    //µTorrent-2.2.1
    //{
    //  "passkey": "xxxx",
    //  "info_hash": "xxx",
    //  "peer_id": "-UT2210-%d6b2%b50%c1%1d%9b%0a%e7%82%c0",
    //  "port": 63636,
    //  "uploaded": 0,
    //  "downloaded": 0,
    //  "left": 135621375,
    //  "corrupt": 0,
    //  "key": "014E25CF",
    //  "event": "started",
    //  "numwant": 200,
    //  "compact": 1,
    //  "no_peer_id": 1,
    //  "ipv6": "210e%1a3"
    //}

    //{
    //  "passkey": "xxx",
    //  "info_hash": "xxx",
    //  "peer_id": "-UT355S-%d8%b5g%1cf%a5%80%c8%16%f4%12%0b",
    //  "port": 24295,
    //  "uploaded": 0,
    //  "downloaded": 0,
    //  "left": 135621375,
    //  "corrupt": 0,
    //  "key": "C394F4CA",
    //  "event": "started",
    //  "numwant": 200,
    //  "compact": 1,
    //  "no_peer_id": 1
    //}

    /**
     * passkey
     */
    private String passkey;

    /**
     * hash
     * 20字节SHA1哈希。请注意，该值将是一个b编码的字典
     */
    private byte[] infoHash;

    /**
     * 客户端ID
     * 客户端在启动时生成的用作客户端唯一ID的url编码的20字节字符串。
     * 它可以是任何值，也可以是二进制数据。它至少必须对您的本地机器是唯一的，因此应该包括诸如进程ID和可能在启动时记录的时间戳之类的东西。
     * 有关此字段的常见客户端编码，请参见下面的peer_id。
     */
    private String peer_id;
    private byte[] peerId;

    /**
     * 客户端ID（16进制）
     * 2d5452343033302d6264637a71111118386a3170
     */
    private String peerIdHex;

    /**
     * 事件
     * started, completed, stopped (或为空，这与未指定相同).
     * 这是一个可选的键，它对应于 started（已开始）、completed（已完成）或 stopped（已停止） （或为空，这等同于不出现）。
     * 如果未指定，则此请求是定期执行的请求之一。
     * 当下载首次开始时，会发送使用 started 的公告，当下载完成时，会发送使用 completed 的公告。
     * 如果文件在开始时就已经完成，那么不会发送 completed。
     * 当下载器停止下载时，会发送使用 stopped 的公告。
     * <p>
     * started：对跟踪器的第一个请求必须包含具有此值的事件键。
     * stopped：如果客户端正常关闭，则必须发送到跟踪器。
     * completed：当下载完成时，必须发送给跟踪器。但是，如果客户端启动时下载已经100%完成，则不得发送。大概是为了允许跟踪器仅根据此事件递增“已完成下载”的度量标准。
     *
     * @see EventType
     */
    private String event = "";

    /**
     * IPv6地址
     */
    private String ipv6;

    /**
     * IPv4地址
     */
    private String ipv4;

    /**
     * IP地址
     * <p>
     * 可选。客户端机器的真实IP地址，以点分四组格式或rfc3513定义的十六进制IPv6地址。
     * 注意：通常不需要此参数，因为可以从发出HTTP请求的IP地址确定客户端的地址。
     * 只有在请求到达的IP地址不是客户端的IP地址的情况下才需要此参数。
     * 这发生在客户端通过代理（或透明Web代理/缓存）与跟踪器通信的情况下。
     * 当客户端和跟踪器都位于NAT网关的同一本地侧时，也是必需的。
     * 这样做的原因是，否则跟踪器将给出客户端的内部（RFC1918）地址，这不是可路由的。
     * 因此，客户端必须显式地声明其（外部，可路由的）IP地址，以便分发给外部对等方。
     * 各种跟踪器对此参数的处理各不相同。有些只有在请求到达的IP地址位于RFC1918空间时才接受它。
     * 其他人无条件地尊重它，而另一些人则完全忽略它。对于IPv6地址（例如：2001:db8:1:2::100），它只表明客户端可以通过IPv6进行通信。
     */
    private String ip;

    /**
     * 密钥
     * <p>
     * 可选。一个不与任何其他客户端共享的附加标识。它旨在允许客户端在IP地址更改时证明其身份。
     */
    private String key;


    /**
     * 端口号
     * 客户端正在侦听的端口号。为BitTorrent保留的端口通常是6881-6889。
     */
    private Integer port;

    /**
     * 需要的同伴数
     * http://bittorrent.org/beps/bep_0008.html
     */
    private Integer numwant = 200;

    /**
     * 已下载量
     * 客户端向跟踪器发送'started'事件后下载的总量（以基数十的ASCII表示）。虽然官方规范中没有明确说明，但共识是这应该是下载的总字节数。
     */
    private Long downloaded = -1L;

    /**
     * 已上传量
     * 客户端向tracker发送'started'事件后上传的总量（以基数十的ASCII表示）。
     * 虽然官方规范中没有明确说明，但共识是这应该是上传的总字节数。
     */
    private Long uploaded = -1L;

    /**
     * 剩余量
     * 此客户端仍需下载的字节数（以基数十的ASCII表示）。澄清：为了达到100%的完成度并获得种子中包含的所有文件，需要下载的字节数。
     */
    private Long left = -1L;

    /**
     * redundant
     * <p>
     * http://bittorrent.org/beps/bep_0016.html
     * <p>
     * 研究或观察表明，通过超级播种模式，初始播种者创建新种子所需的上传数据量仅为总文件大小的大约105%，而使用标准方法通常需要150-200%。
     * 一般用途不建议使用超级种子模式。虽然它确实有助于更广泛地分发稀有数据，
     * 但因为它限制了客户端可以下载的片段的选择，所以它也限制了这些客户端下载他们已经部分检索的片段的数据的能力。
     * 因此，仅建议初始种子服务器使用超级种子模式。
     */
    private Long redundant = 0L;

    /**
     * 压缩响应
     * 将此设置为1表示客户端接受紧凑的响应。
     * <p>
     * 相关文档
     * http://bittorrent.org/beps/bep_0007.html
     * http://bittorrent.org/beps/bep_0010.html
     * http://bittorrent.org/beps/bep_0015.html
     * http://bittorrent.org/beps/bep_0052.html
     * http://bittorrent.org/beps/bep_0023.html
     */
    private Integer compact;

    /**
     * "no_peer_id"：表示tracker可以在peers字典中省略peer id字段。如果启用了"compact"选项，则会忽略此选项。
     */
    private Integer no_peer_id;

    /**
     * 做种
     */
    private Boolean seeder;

    /**
     * 远程地址
     */
    private String remoteAddr;

    /**
     * 客户端信息
     */
    private String userAgent;

    /**
     * 想要的摘要信息
     */
    private String wantDigest;

    /**
     * 用户实体
     */
    private UserEntity user;

    /**
     * 用户实体
     */
    private TorrentEntity torrent;


    public boolean isNoPeerId() {
        boolean noPeerId = Integer.valueOf(1).equals(getNo_peer_id());

        return noPeerId;
    }

    /**
     * 事件
     * started, completed, stopped (或为空，这与未指定相同).
     * 这是一个可选的键，它对应于 started（已开始）、completed（已完成）或 stopped（已停止） （或为空，这等同于不出现）。
     * 如果未指定，则此请求是定期执行的请求之一。
     * 当下载首次开始时，会发送使用 started 的公告，当下载完成时，会发送使用 completed 的公告。
     * 如果文件在开始时就已经完成，那么不会发送 completed。
     * 当下载器停止下载时，会发送使用 stopped 的公告。
     * <p>
     * started：对跟踪器的第一个请求必须包含具有此值的事件键。
     * stopped：如果客户端正常关闭，则必须发送到跟踪器。
     * completed：当下载完成时，必须发送给跟踪器。但是，如果客户端启动时下载已经100%完成，则不得发送。大概是为了允许跟踪器仅根据此事件递增“已完成下载”的度量标准。
     * paused： http://bittorrent.org/beps/bep_0021.html
     */
    public interface EventType {
        // started, completed, stopped (或为空，这与未指定相同).
        //
        String started = "started";
        String completed = "completed";
        String stopped = "stopped";
        String paused = "paused";
        String empty = "";
    }

}
