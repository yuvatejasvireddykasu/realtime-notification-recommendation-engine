package com.realtime.notificationengine.service;

import com.realtime.notificationengine.model.UserEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EventService {

    private final EventProducer eventProducer;

    public EventService(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    public Mono<UserEvent> ingest(UserEvent event) {
        return eventProducer.publish(event).thenReturn(event);
    }
}
