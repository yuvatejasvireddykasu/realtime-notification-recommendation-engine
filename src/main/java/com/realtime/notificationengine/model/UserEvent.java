package com.realtime.notificationengine.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {

    @Id
    @Column(name = "event_id", nullable = false, updatable = false)
    private String eventId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private Instant timestamp;

    public static UserEvent of(String userId, String eventType, String productId) {
        return UserEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .userId(userId)
                .eventType(eventType)
                .productId(productId)
                .timestamp(Instant.now())
                .build();
    }
}
