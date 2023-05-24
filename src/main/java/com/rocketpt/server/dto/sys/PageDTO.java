package com.rocketpt.server.dto.sys;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author plexpt
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {

    List<T> list;


    long total;

}
