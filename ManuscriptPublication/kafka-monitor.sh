#!/bin/bash

# Kafka 이벤트 모니터링 스크립트

echo "🔍 Kafka 이벤트 모니터링 도구"
echo "==============================="

# Kafka 컨테이너 상태 확인
echo "1️⃣ Kafka 컨테이너 상태 확인..."
if docker ps | grep -q kafka; then
    echo "✅ Kafka 컨테이너 실행 중"
    KAFKA_CONTAINER=$(docker ps | grep kafka | awk '{print $1}')
    echo "📦 컨테이너 ID: $KAFKA_CONTAINER"
else
    echo "❌ Kafka 컨테이너가 실행되지 않음"
    echo "💡 Kafka 실행: cd infra && docker-compose up -d"
    exit 1
fi

echo ""

# 토픽 목록 확인
echo "2️⃣ 사용 가능한 토픽 목록..."
docker exec $KAFKA_CONTAINER kafka-topics --list --bootstrap-server localhost:9092

echo ""

# 메뉴 선택
echo "📋 선택하세요:"
echo "1. 실시간 이벤트 모니터링 (최신 이벤트만)"
echo "2. 전체 이벤트 히스토리 보기 (처음부터)"
echo "3. 특정 이벤트 타입으로 필터링"
echo "4. 이벤트 개수 확인"
echo "5. 종료"

read -p "선택 (1-5): " choice

case $choice in
    1)
        echo ""
        echo "🔄 실시간 이벤트 모니터링 시작..."
        echo "💡 Ctrl+C로 종료"
        echo "-----------------------------------"
        docker exec -it $KAFKA_CONTAINER kafka-console-consumer \
            --bootstrap-server localhost:9092 \
            --topic ktaivleminitocode
        ;;
    2)
        echo ""
        echo "📜 전체 이벤트 히스토리 조회..."
        echo "💡 Ctrl+C로 종료"
        echo "-----------------------------------"
        docker exec -it $KAFKA_CONTAINER kafka-console-consumer \
            --bootstrap-server localhost:9092 \
            --topic ktaivleminitocode \
            --from-beginning
        ;;
    3)
        echo ""
        echo "🔍 필터링할 이벤트 타입을 선택하세요:"
        echo "1. PublicationRequested"
        echo "2. GptContentGenerationStarted"
        echo "3. RegisterBookRequested"
        echo "4. BookPublished"
        read -p "선택 (1-4): " filter_choice

        case $filter_choice in
            1) FILTER="PublicationRequested" ;;
            2) FILTER="GptContentGenerationStarted" ;;
            3) FILTER="RegisterBookRequested" ;;
            4) FILTER="BookPublished" ;;
            *) echo "❌ 잘못된 선택"; exit 1 ;;
        esac

        echo ""
        echo "🔄 '$FILTER' 이벤트만 필터링..."
        echo "💡 Ctrl+C로 종료"
        echo "-----------------------------------"
        docker exec -it $KAFKA_CONTAINER kafka-console-consumer \
            --bootstrap-server localhost:9092 \
            --topic ktaivleminitocode \
            --from-beginning | grep "$FILTER"
        ;;
    4)
        echo ""
        echo "📊 저장된 이벤트 개수 확인..."

        # 파티션별 오프셋 확인
        docker exec $KAFKA_CONTAINER kafka-run-class kafka.tools.GetOffsetShell \
            --broker-list localhost:9092 \
            --topic ktaivleminitocode

        echo ""
        echo "📈 최근 이벤트 타입별 개수 (최근 10개):"
        docker exec $KAFKA_CONTAINER kafka-console-consumer \
            --bootstrap-server localhost:9092 \
            --topic ktaivleminitocode \
            --from-beginning \
            --max-messages 10 2>/dev/null | \
            grep -o '"eventType":"[^"]*"' | \
            sort | uniq -c
        ;;
    5)
        echo "👋 모니터링 종료"
        exit 0
        ;;
    *)
        echo "❌ 잘못된 선택"
        exit 1
        ;;
esac
