package com.realtime.notificationengine.controller;

import com.realtime.notificationengine.dto.RecommendationResponse;
import com.realtime.notificationengine.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public Mono<RecommendationResponse> getRecommendations(@PathVariable String userId) {
        return recommendationService.getRecommendations(userId);
    }
}
