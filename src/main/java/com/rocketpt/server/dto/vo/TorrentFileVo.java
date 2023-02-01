package com.rocketpt.server.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TorrentFileVo {
    private String fillName;
    private String announce;
    private byte[] hash;
    private long length;
}
