package com.rocketpt.server.service.infra;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.common.EventStore;
import com.rocketpt.server.dao.StoredEventDao;
import com.rocketpt.server.dto.entity.StoredEventEntity;
import com.rocketpt.server.util.JsonUtils;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@Service
@RequiredArgsConstructor
public class EventStoreService implements EventStore {

    private final StoredEventDao storedEventDao;

    @Override
    public void append(DomainEvent aDomainEvent) {
        if (!aDomainEvent.needSave()) {
            return;
        }

        StoredEventEntity storedEventEntity = new StoredEventEntity();
        storedEventEntity.setEventBody(JsonUtils.stringify(aDomainEvent));
        storedEventEntity.setOccurredOn(aDomainEvent.occurredOn());
        storedEventEntity.setTypeName(aDomainEvent.getClass().getTypeName());
        storedEventDao.insert(storedEventEntity);
    }

}
