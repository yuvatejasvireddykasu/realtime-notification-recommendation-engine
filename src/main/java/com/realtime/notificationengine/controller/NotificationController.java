package com.realtime.notificationengine.controller;

import com.realtime.notificationengine.dto.NotificationResponse;
import com.realtime.notificationengine.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public Mono<NotificationResponse> getNotifications(@PathVariable String userId) {
        return notificationService.getNotifications(userId);
    }
}
