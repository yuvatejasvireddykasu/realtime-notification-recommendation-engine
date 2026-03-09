package com.realtime.notificationengine.kafka;

import com.realtime.notificationengine.model.UserEvent;
import com.realtime.notificationengine.service.NotificationService;
import com.realtime.notificationengine.service.RecommendationService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    private final RecommendationService recommendationService;
    private final NotificationService notificationService;
    private final MeterRegistry registry;

    public EventConsumer(RecommendationService recommendationService,
                         NotificationService notificationService,
                         MeterRegistry registry) {
        this.recommendationService = recommendationService;
        this.notificationService = notificationService;
        this.registry = registry;
    }

    @KafkaListener(topics = "${app.kafka.topic.user-events}", groupId = "recommendation-service-group")
    public void consumeForRecommendation(UserEvent event) {
        long start = System.nanoTime();
        recommendationService.processEvent(event).block();
        registry.timer("event.latency", "consumer", "recommendation")
                .record(System.nanoTime() - start, java.util.concurrent.TimeUnit.NANOSECONDS);
    }

    @KafkaListener(topics = "${app.kafka.topic.user-events}", groupId = "notification-service-group")
    public void consumeForNotification(UserEvent event) {
        long start = System.nanoTime();
        notificationService.processEvent(event).block();
        registry.timer("event.latency", "consumer", "notification")
                .record(System.nanoTime() - start, java.util.concurrent.TimeUnit.NANOSECONDS);
    }
}
