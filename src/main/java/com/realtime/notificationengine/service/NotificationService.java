package com.realtime.notificationengine.service;

import com.realtime.notificationengine.dto.NotificationResponse;
import com.realtime.notificationengine.model.Notification;
import com.realtime.notificationengine.model.UserEvent;
import com.realtime.notificationengine.repository.NotificationRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final Counter notificationSuccess;

    public NotificationService(NotificationRepository notificationRepository, MeterRegistry registry) {
        this.notificationRepository = notificationRepository;
        this.notificationSuccess = registry.counter("notification.success");
    }

    public Mono<Void> processEvent(UserEvent event) {
        return Mono.fromRunnable(() -> {
                    String message = "Users also viewed product " + event.getProductId();
                    Notification notification = Notification.builder()
                            .userId(event.getUserId())
                            .message(message)
                            .createdAt(Instant.now())
                            .read(false)
                            .build();
                    notificationRepository.save(notification);
                    sendNotification(message, event.getUserId());
                    notificationSuccess.increment();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    public Mono<NotificationResponse> getNotifications(String userId) {
        return Mono.fromCallable(() -> notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                        .stream()
                        .map(Notification::getMessage)
                        .toList())
                .subscribeOn(Schedulers.boundedElastic())
                .map(NotificationResponse::new);
    }

    private void sendNotification(String message, String userId) {
        log.info("Notification sent to userId={} message={}", userId, message);
    }
}
