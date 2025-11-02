package com.realtime.notificationengine.service;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisPublisherService {

    private final ReactiveRedisTemplate<String, String> redisTemplate;


    public RedisPublisherService(ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public Mono<Long> publish(String channel, String message) {
        return redisTemplate.convertAndSend(channel, message)
                .doOnSuccess(count -> System.out.println("📢 Published message to channel '" + channel + "'"))
                .doOnError(e -> System.err.println("❌ Failed to publish message: " + e.getMessage()));
    }
}
