package com.realtime.notificationengine.controller;

import com.realtime.notificationengine.dto.EventRequest;
import com.realtime.notificationengine.model.UserEvent;
import com.realtime.notificationengine.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<UserEvent> publishEvent(@Valid @RequestBody EventRequest request) {
        return eventService.ingest(UserEvent.of(request.userId(), request.eventType(), request.productId()));
    }
}
