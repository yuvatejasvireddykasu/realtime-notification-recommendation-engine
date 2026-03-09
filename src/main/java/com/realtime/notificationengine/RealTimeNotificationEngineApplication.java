package com.realtime.notificationengine;

import com.realtime.notificationengine.config.AppTopicsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppTopicsProperties.class)
public class RealTimeNotificationEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealTimeNotificationEngineApplication.class, args);
    }
}
