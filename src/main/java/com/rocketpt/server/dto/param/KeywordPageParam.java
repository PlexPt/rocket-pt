package com.rocketpt.server.dto.param;


import com.rocketpt.server.common.base.PageParam;

import lombok.Data;

@Data
public class KeywordPageParam extends PageParam {

    String keyword;
}
