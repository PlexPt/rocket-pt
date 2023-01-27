package com.rocketpt.server.sys.entity;


import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * @author plexpt
 */
@Data
@TableName("stored_event")
public class StoredEvent extends EntityBase {

    private String eventBody;
    private LocalDateTime occurredOn;
    private String typeName;

}
