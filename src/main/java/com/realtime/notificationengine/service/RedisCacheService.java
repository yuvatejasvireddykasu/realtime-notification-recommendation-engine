package com.realtime.notificationengine.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisCacheService {

    @Qualifier("reactiveRedisTemplate")
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public RedisCacheService(ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<Boolean> save(String key, String value) {
        return redisTemplate.opsForValue().set(key, value);
    }

    public Mono<String> get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

}
