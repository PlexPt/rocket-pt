package com.rocketpt.server.dto.sys;

import java.util.List;

/**
 * @author plexpt
 */
public record PageDTO<T>(List<T> list, long total) {

}
