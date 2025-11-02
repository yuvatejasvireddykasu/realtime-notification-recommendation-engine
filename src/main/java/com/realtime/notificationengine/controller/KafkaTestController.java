package com.realtime.notificationengine.controller;

import com.realtime.notificationengine.model.NotificationEvent;
import com.realtime.notificationengine.service.KafkaProducerService;
import com.realtime.notificationengine.service.NotificationEventService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class KafkaTestController {

    private final NotificationEventService notificationEventService;

    public KafkaTestController(NotificationEventService notificationEventService) {
        this.notificationEventService = notificationEventService;
    }

    @PostMapping("/publish")
    public Mono<String> publishEvent(@RequestBody NotificationEvent event) {
        return notificationEventService.publishEvent("realtime-notification-topic", event);
    }
}
