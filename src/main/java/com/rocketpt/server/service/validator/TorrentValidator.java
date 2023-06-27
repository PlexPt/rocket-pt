package com.rocketpt.server.service.validator;

import com.rocketpt.server.common.exception.TrackerException;
import com.rocketpt.server.dto.entity.TorrentEntity;
import com.rocketpt.server.dto.param.AnnounceRequest;
import com.rocketpt.server.service.TorrentService;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class TorrentValidator implements TrackerValidator {


    final TorrentService torrentService;

    @Override
    public void validate(AnnounceRequest request) {
        TorrentEntity torrentEntity = torrentService.getByInfoHash(request.getInfoHash());

        if (torrentEntity == null) {
            throw new TrackerException("Torrent is not authorized for use on this tracker");
        }


        if (!torrentEntity.isStatusOK()) {
            throw new TrackerException("Torrent status not ok, please keep seeding.");
        }


        request.setTorrent(torrentEntity);

    }

    @Override
    public int getOrder() {
        return 101;
    }


}
