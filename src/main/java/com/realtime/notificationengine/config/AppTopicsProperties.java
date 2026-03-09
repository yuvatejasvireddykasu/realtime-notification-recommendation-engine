package com.realtime.notificationengine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka.topic")
public record AppTopicsProperties(String userEvents, String userEventsDlt) {
}
