package com.realtime.notificationengine.service;

import com.realtime.notificationengine.model.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    public Mono<String> sendEvent(String topic, String  event) {
        return Mono.fromFuture(kafkaTemplate.send(topic, event).toCompletableFuture())
                   .map(result ->{
                       log.info("✅ Sent event: {} to topic: {}", event, topic);
                       return "Message sent to topic: " + result.getRecordMetadata().topic();})
                   .doOnError(Throwable::printStackTrace)
                   .onErrorResume(e-> Mono.just("Failed to send message: " + e.getMessage()));
    }
}
