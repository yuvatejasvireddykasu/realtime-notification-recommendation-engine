package com.realtime.notificationengine.config;

import com.realtime.notificationengine.model.UserEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    @Bean
    NewTopic userEventsTopic(AppTopicsProperties topics) {
        return TopicBuilder.name(topics.userEvents()).partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic userEventsDltTopic(AppTopicsProperties topics) {
        return TopicBuilder.name(topics.userEventsDlt()).partitions(3).replicas(1).build();
    }

    @Bean
    DefaultErrorHandler kafkaErrorHandler(KafkaOperations<String, UserEvent> operations, AppTopicsProperties topics) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                operations,
                (record, ex) -> new TopicPartition(topics.userEventsDlt(), record.partition())
        );
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1_000L, 3));
    }
}
