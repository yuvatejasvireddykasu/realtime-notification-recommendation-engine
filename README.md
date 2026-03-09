# Real-Time Notification & Recommendation Engine

Reactive backend built on Java 17 + Spring Boot + WebFlux with Kafka, Redis, and PostgreSQL.

## Features
- `POST /events`: validates and publishes user events to Kafka topic `user-events`.
- Dual Kafka consumers:
  - Recommendation consumer: stores generated recommendations in Redis (`rec:{userId}` with 10-minute TTL) and PostgreSQL.
  - Notification consumer: stores and emits user notifications.
- `GET /recommendations/{userId}`: Redis-first cache strategy, recompute on cache miss.
- `GET /notifications/{userId}`: fetches persisted notifications.
- Fault tolerance via Kafka retries, `DefaultErrorHandler`, and dead-letter topic (`user-events-dlt`).
- Observability via Actuator + Prometheus metrics and Grafana/Prometheus docker services.

## Run dependencies
```bash
docker compose up -d zookeeper kafka redis postgres prometheus grafana
```

## Run application
```bash
./gradlew bootRun
```

## Example requests
```bash
curl -X POST http://localhost:8080/events \
  -H 'Content-Type: application/json' \
  -d '{"userId":"101","eventType":"PRODUCT_VIEW","productId":"567"}'

curl http://localhost:8080/recommendations/101
curl http://localhost:8080/notifications/101
```
