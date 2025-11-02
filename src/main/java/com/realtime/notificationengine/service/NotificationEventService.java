package com.realtime.notificationengine.service;

import com.realtime.notificationengine.model.NotificationEvent;
import com.realtime.notificationengine.util.JsonUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotificationEventService {

    private final KafkaProducerService kafkaProducerService;

    public NotificationEventService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public Mono<String> publishEvent(String topic ,NotificationEvent event) {
        return kafkaProducerService.sendEvent(topic, JsonUtil.toJson(event));
    }
}
