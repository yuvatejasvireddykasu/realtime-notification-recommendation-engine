package com.realtime.notificationengine.service;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class RedisSubscriberService {

    private final ReactiveStringRedisTemplate redisTemplate;

    public RedisSubscriberService(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void subscribe() {
        redisTemplate.listenTo(ChannelTopic.of("notifications"))
                .doOnNext(message -> System.out.println("📨 Received via Redis Pub/Sub: " + message.getMessage()))
                .subscribe();
    }
}
