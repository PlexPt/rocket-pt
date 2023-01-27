package com.rocketpt.server.sys.dto;

import java.util.List;

/**
 * @author plexpt
 */
public record PageDTO<T>(List<T> list, long total) {

}
