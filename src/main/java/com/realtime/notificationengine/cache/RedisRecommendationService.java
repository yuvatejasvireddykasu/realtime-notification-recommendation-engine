package com.realtime.notificationengine.cache;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Service
public class RedisRecommendationService {

    private static final Duration TTL = Duration.ofMinutes(10);
    private final ReactiveStringRedisTemplate redisTemplate;

    public RedisRecommendationService(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<List<String>> getRecommendations(String userId) {
        return redisTemplate.opsForValue()
                .get(key(userId))
                .map(value -> Arrays.asList(value.split(",")));
    }

    public Mono<Boolean> putRecommendations(String userId, List<String> recommendations) {
        return redisTemplate.opsForValue()
                .set(key(userId), String.join(",", recommendations), TTL);
    }

    private String key(String userId) {
        return "rec:" + userId;
    }
}
