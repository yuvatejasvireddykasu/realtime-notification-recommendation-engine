package com.realtime.notificationengine.service;

import com.realtime.notificationengine.model.NotificationEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotificationRouterService {

    private final RedisCacheService redisCacheService;

    private final RedisPublisherService redisPublisherService;

    public NotificationRouterService(RedisCacheService redisCacheService, RedisPublisherService redisPublisherService) {
        this.redisCacheService = redisCacheService;
        this.redisPublisherService = redisPublisherService;
    }

    public Mono<Void> route(NotificationEvent event) {
        return switch (event.getType()) {
            case "PRICE_DROP" -> handlePriceDrop(event);
            case "ORDER_UPDATE" -> handleOrderUpdate(event);
            default -> handleGeneric(event);
        };
    }

    private Mono<Void> handlePriceDrop(NotificationEvent event) {
        String key = "promo:" + event.getRecipient() + ":" + event.getId();
        System.out.println("📉 Handling PRICE_DROP for user " + event.getRecipient());
        return redisCacheService.save(key, event.toString()).then()
                .then(redisPublisherService.publish("notifications", event.toString()))
                .then();
    }

    private Mono<Void> handleOrderUpdate(NotificationEvent event) {
        String key = "order:" + event.getRecipient() + ":" + event.getId();
        System.out.println("📦 Handling ORDER_UPDATE for user " + event.getRecipient());
        return redisCacheService.save(key, event.toString()).then()
                .then(redisPublisherService.publish("notifications", event.toString()))
                .then();
    }

    private Mono<Void> handleGeneric(NotificationEvent event) {
        String key = "generic:" + event.getRecipient() + ":" + event.getId();
        System.out.println("📬 Handling GENERIC notification for user " + event.getRecipient());
        return redisCacheService.save(key, event.toString()).then()
                .then(redisPublisherService.publish("notifications", event.toString()))
                .then();
    }
}
