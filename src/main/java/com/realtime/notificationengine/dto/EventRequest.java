package com.realtime.notificationengine.dto;

import jakarta.validation.constraints.NotBlank;

public record EventRequest(
        @NotBlank String userId,
        @NotBlank String eventType,
        @NotBlank String productId
) {
}
