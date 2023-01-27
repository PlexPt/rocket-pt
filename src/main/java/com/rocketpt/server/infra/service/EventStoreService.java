package com.rocketpt.server.infra.service;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.common.EventStore;
import com.rocketpt.server.common.JsonUtils;
import com.rocketpt.server.sys.entity.StoredEvent;
import com.rocketpt.server.sys.repository.StoredEventRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@Service
@RequiredArgsConstructor
public class EventStoreService implements EventStore {

    private final StoredEventRepository storedEventRepository;

    @Override
    public void append(DomainEvent aDomainEvent) {
        StoredEvent storedEvent = new StoredEvent();
        storedEvent.setEventBody(JsonUtils.stringify(aDomainEvent));
        storedEvent.setOccurredOn(aDomainEvent.occurredOn());
        storedEvent.setTypeName(aDomainEvent.getClass().getTypeName());
        storedEventRepository.insert(storedEvent);
    }

}
