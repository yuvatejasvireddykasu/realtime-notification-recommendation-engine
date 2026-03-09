package com.realtime.notificationengine.dto;

import java.util.List;

public record RecommendationResponse(String userId, List<String> recommendations) {
}
