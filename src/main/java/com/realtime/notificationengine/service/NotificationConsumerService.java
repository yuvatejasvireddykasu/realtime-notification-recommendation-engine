package com.realtime.notificationengine.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationConsumerService {

    private final RedisCacheService redisCacheService;

    public NotificationConsumerService(RedisCacheService redisCacheService) {
        this.redisCacheService = redisCacheService;
    }


}
