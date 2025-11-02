package com.realtime.notificationengine.controller;

import com.realtime.notificationengine.model.CacheRequest;
import com.realtime.notificationengine.service.RedisCacheService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cache")
public class RedisController {
    private RedisCacheService redisCacheService;

    public RedisController(RedisCacheService redisCacheService) {
        this.redisCacheService = redisCacheService;
    }
    @GetMapping("/get")
    public Mono<String> getValue(@RequestParam  String key) {
        return redisCacheService.get(key)
                .map(value -> "Value for key '" + key + "': " + value)
                .switchIfEmpty(Mono.just("No value found for key '" + key + "'"));
    }

    @PostMapping("/save")
    public Mono<String> saveValue(@RequestBody CacheRequest request) {
        return redisCacheService.save(request.getKey(), request.getValue())
                .map(success -> success ? "Value saved successfully" : "Failed to save value");
    }
}
