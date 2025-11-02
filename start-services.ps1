Write-Host "🟡 Starting Zookeeper..."
docker run -d --name zookeeper -p 2181:2181 `
  -e ZOOKEEPER_CLIENT_PORT=2181 `
  -e ZOOKEEPER_TICK_TIME=2000 `
  confluentinc/cp-zookeeper:7.7.0

Start-Sleep -Seconds 10

Write-Host "🟢 Starting Kafka..."
docker run -d --name kafka -p 9092:9092 `
  -e KAFKA_BROKER_ID=1 `
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 `
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 `
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT `
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 `
  --network bridge `
  confluentinc/cp-kafka:7.7.0

Start-Sleep -Seconds 5

Write-Host "🔵 Starting Redis..."
docker run -d --name redis -p 6379:6379 redis:latest

Write-Host "✅ All services started!"
docker ps
