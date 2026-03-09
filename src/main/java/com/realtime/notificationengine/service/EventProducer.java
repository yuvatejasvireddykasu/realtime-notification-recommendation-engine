package com.realtime.notificationengine.service;

import com.realtime.notificationengine.config.AppTopicsProperties;
import com.realtime.notificationengine.model.UserEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class EventProducer {

    private static final Logger log = LoggerFactory.getLogger(EventProducer.class);

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private final AppTopicsProperties topics;
    private final Counter publishFailures;

    public EventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate,
                         AppTopicsProperties topics,
                         MeterRegistry registry) {
        this.kafkaTemplate = kafkaTemplate;
        this.topics = topics;
        this.publishFailures = registry.counter("events.publish.failures");
    }

    public Mono<Void> publish(UserEvent event) {
        return Mono.fromFuture(kafkaTemplate.send(topics.userEvents(), event.getUserId(), event))
                .doOnSuccess(result -> log.info("Published event {} to partition {}", event.getEventId(), result.getRecordMetadata().partition()))
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(250)))
                .doOnError(ex -> {
                    publishFailures.increment();
                    log.error("Failed publishing event {}", event.getEventId(), ex);
                })
                .then();
    }
}
