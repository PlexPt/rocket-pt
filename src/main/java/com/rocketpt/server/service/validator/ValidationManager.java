package com.rocketpt.server.service.validator;


import com.rocketpt.server.dto.param.AnnounceRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidationManager {

    List<TrackerValidator> validators;

    @Autowired
    public ValidationManager(List<TrackerValidator> validatorList) {
        this.validators = validatorList.stream()
                .sorted(Comparator.comparing(TrackerValidator::getOrder))
                .collect(Collectors.toList());
    }


    public void validate(AnnounceRequest object) {
        for (TrackerValidator validator : validators) {
            validator.validate(object);
        }
    }

}
