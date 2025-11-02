package com.realtime.notificationengine.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDateTime;

@With
@Data
@Builder
public class NotificationEvent {
    private String id;              // Unique ID for the notification
    private String type;            // Type: ORDER_UPDATE, PRICE_DROP, etc.
    private String message;         // Main message content
    private String recipient;       // Who should receive it (e.g., userId)
    private LocalDateTime timestamp;      // When it was generated
    private String sourceSystem;    // Optional: which microservice emitted it
    private String priority;
}
