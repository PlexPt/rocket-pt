package com.rocketpt.server.common;


/**
 * @author plexpt
 * @date 2022/7/22
 */
public interface EventStore {

    void append(DomainEvent aDomainEvent);

}
