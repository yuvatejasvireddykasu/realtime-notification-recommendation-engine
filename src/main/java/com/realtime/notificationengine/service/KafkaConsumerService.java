package com.realtime.notificationengine.service;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtime.notificationengine.model.NotificationEvent;
import com.realtime.notificationengine.util.JsonUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class KafkaConsumerService {

    private final NotificationRouterService notificationRouterService;



    private Predicate<NotificationEvent> isValid =
            event -> event != null && event.getId() != null;

    public KafkaConsumerService(NotificationRouterService notificationRouterService) {
        this.notificationRouterService = notificationRouterService;
    }


    @KafkaListener(topics = "realtime-notification-topic", groupId = "realtime-engine-group-v2")
    public void consumeMessage( String event) {
        System.out.println("Consumed message from : " + event);
        Mono.fromSupplier(()->processMessage(event))
                .filter(isValid)
                .flatMap(notificationRouterService::route)
                .subscribe();
    }

    private NotificationEvent processMessage(String message) {
        return  JsonUtil.fromJson(message, NotificationEvent.class);
    }
}
