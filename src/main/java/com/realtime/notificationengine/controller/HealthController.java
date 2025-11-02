package com.realtime.notificationengine.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthController {

    @GetMapping("api/health")
    public Mono<String> healthCheck(){
        return Mono.just("Notification Engine is up and running!");
    }
}
