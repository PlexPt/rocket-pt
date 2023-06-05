package com.rocketpt.server.common.exception;

import com.rocketpt.server.controller.TrackerController;
import com.rocketpt.server.util.BencodeUtil;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * @author plexpt
 */
@Slf4j
@RestControllerAdvice(assignableTypes = TrackerController.class)
public class ExceptionControllerAdviceTracker {


    @ExceptionHandler(TrackerException.class)
    public String invalidLengthExceptionHandler(TrackerException exception) {
        return BencodeUtil.error(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public String e(Exception exception) {
        return BencodeUtil.error(exception.getMessage());
    }


}

