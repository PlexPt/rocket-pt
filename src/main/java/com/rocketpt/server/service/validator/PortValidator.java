package com.rocketpt.server.service.validator;

import com.rocketpt.server.common.exception.TrackerException;
import com.rocketpt.server.dto.param.AnnounceRequest;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PortValidator implements TrackerValidator {


    List<Integer> banned = List.of(411, 412, 413,
            6881, 6882, 6883, 6884, 6885, 6886, 6887, 6888, 6889, 1214, 6346, 6347, 4662, 6699);

    @Override
    public void validate(AnnounceRequest request) {

        if (request.getPort() == null) {
            throw new TrackerException("Port null");
        }

        if (request.getPort() <= 0 || request.getPort() >= 65535) {
            throw new TrackerException("Port " + request.getPort() + " is invalid");
        }


        for (Integer port : banned) {

            if (port.equals(request.getPort())) {
                throw new TrackerException("Port " + request.getPort() + " is banned.");
            }

        }

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
