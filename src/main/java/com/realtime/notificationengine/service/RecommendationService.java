package com.realtime.notificationengine.service;

import com.realtime.notificationengine.cache.RedisRecommendationService;
import com.realtime.notificationengine.dto.RecommendationResponse;
import com.realtime.notificationengine.model.Recommendation;
import com.realtime.notificationengine.model.UserEvent;
import com.realtime.notificationengine.repository.RecommendationRepository;
import com.realtime.notificationengine.repository.UserEventRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {

    private final RedisRecommendationService redisRecommendationService;
    private final RecommendationRepository recommendationRepository;
    private final UserEventRepository userEventRepository;
    private final Timer recommendationTimer;

    public RecommendationService(RedisRecommendationService redisRecommendationService,
                                 RecommendationRepository recommendationRepository,
                                 UserEventRepository userEventRepository,
                                 MeterRegistry registry) {
        this.redisRecommendationService = redisRecommendationService;
        this.recommendationRepository = recommendationRepository;
        this.userEventRepository = userEventRepository;
        this.recommendationTimer = registry.timer("recommendation.generation.time");
    }

    public Mono<RecommendationResponse> getRecommendations(String userId) {
        return redisRecommendationService.getRecommendations(userId)
                .map(list -> new RecommendationResponse(userId, list))
                .switchIfEmpty(Mono.defer(() -> recomputeAndCache(userId)
                        .map(list -> new RecommendationResponse(userId, list))));
    }

    public Mono<Void> processEvent(UserEvent event) {
        return Mono.fromRunnable(() -> {
                    userEventRepository.save(event);
                    List<String> recommendations = buildRecommendationsFromProduct(event.getProductId());
                    persistRecommendations(event.getUserId(), recommendations);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then(redisRecommendationService.putRecommendations(event.getUserId(), buildRecommendationsFromProduct(event.getProductId())))
                .then();
    }

    private Mono<List<String>> recomputeAndCache(String userId) {
        return Mono.fromCallable(() -> {
                    Instant start = Instant.now();
                    List<UserEvent> events = userEventRepository.findTop20ByUserIdOrderByTimestampDesc(userId);
                    String baseProduct = events.isEmpty() ? "100" : events.get(0).getProductId();
                    List<String> generated = buildRecommendationsFromProduct(baseProduct);
                    persistRecommendations(userId, generated);
                    recommendationTimer.record(java.time.Duration.between(start, Instant.now()));
                    return generated;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(items -> redisRecommendationService.putRecommendations(userId, items).thenReturn(items));
    }

    private void persistRecommendations(String userId, List<String> products) {
        recommendationRepository.saveAll(products.stream().map(product -> Recommendation.builder()
                        .userId(userId)
                        .productId(product)
                        .score(0.95)
                        .createdAt(Instant.now())
                        .build())
                .toList());
    }

    private List<String> buildRecommendationsFromProduct(String productId) {
        int base = Integer.parseInt(productId);
        List<String> recs = new ArrayList<>();
        recs.add(String.valueOf(base + 323));
        recs.add(String.valueOf(base + 334));
        recs.add(String.valueOf(base + 210));
        return recs;
    }
}
