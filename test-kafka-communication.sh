#!/bin/bash

# Kafka 통신 및 서비스 간 연동 테스트 스크립트

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🔍 Kafka 통신 및 서비스 연동 테스트${NC}"
echo "=========================================="

# 1. Kafka 상태 확인
echo -e "${YELLOW}1️⃣ Kafka 서버 상태 확인${NC}"
if docker ps | grep -q kafka; then
    KAFKA_CONTAINER=$(docker ps | grep kafka | awk '{print $1}')
    echo -e "${GREEN}✅ Kafka 컨테이너 실행 중: $KAFKA_CONTAINER${NC}"

    # 토픽 목록 확인
    echo -e "${BLUE}📋 사용 가능한 토픽 목록:${NC}"
    docker exec $KAFKA_CONTAINER kafka-topics --list --bootstrap-server localhost:9092

    # 토픽 생성 (존재하지 않는 경우)
    echo -e "${YELLOW}📝 기본 토픽 생성/확인${NC}"
    docker exec $KAFKA_CONTAINER kafka-topics --create --topic ktaivleminitocode --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1 --if-not-exists
    echo -e "${GREEN}✅ 토픽 준비 완료${NC}"
else
    echo -e "${RED}❌ Kafka 컨테이너가 실행되지 않음${NC}"
    echo -e "${BLUE}💡 Kafka 시작: cd infra && docker-compose up -d${NC}"
    exit 1
fi

# 2. 각 서비스 헬스체크
echo -e "\n${YELLOW}2️⃣ 마이크로서비스 헬스체크${NC}"

services=(
    "SubscriptionManagement:8080"
    "AuthorManagement:8082"
    "librarymanagement:8084"
    "pointmanagement:8085"
    "ManuscriptPublication:8086"
    "gateway:8088"
)

for service_port in "${services[@]}"; do
    IFS=':' read -r service port <<< "$service_port"
    echo -e "${BLUE}🔍 $service (포트: $port) 확인 중...${NC}"

    if curl -s -f "http://localhost:$port/actuator/health" > /dev/null 2>&1; then
        response=$(curl -s "http://localhost:$port/actuator/health")
        echo -e "${GREEN}✅ $service 정상 동작${NC}"
        echo "   응답: $response"
    else
        echo -e "${RED}❌ $service 연결 실패 (포트: $port)${NC}"
        echo -e "${BLUE}💡 서비스 시작: cd $service && mvn spring-boot:run${NC}"
    fi
done

# 3. Kafka 이벤트 모니터링 설정
echo -e "\n${YELLOW}3️⃣ Kafka 이벤트 모니터링${NC}"
echo -e "${BLUE}📡 실시간 이벤트 모니터링을 시작하시겠습니까? (y/n)${NC}"
read -r monitor_choice

if [[ $monitor_choice == "y" || $monitor_choice == "Y" ]]; then
    echo -e "${GREEN}🔄 실시간 Kafka 이벤트 모니터링 시작...${NC}"
    echo -e "${YELLOW}💡 Ctrl+C로 모니터링을 종료할 수 있습니다${NC}"
    echo "-----------------------------------"

    # 백그라운드에서 이벤트 모니터링
    docker exec -it $KAFKA_CONTAINER kafka-console-consumer \
        --bootstrap-server localhost:9092 \
        --topic ktaivleminitocode \
        --from-beginning
fi

# 4. 테스트 이벤트 발송
echo -e "\n${YELLOW}4️⃣ 테스트 이벤트 발송${NC}"
echo -e "${BLUE}📤 테스트 이벤트를 발송하시겠습니까? (y/n)${NC}"
read -r send_choice

if [[ $send_choice == "y" || $send_choice == "Y" ]]; then
    echo -e "${GREEN}📤 테스트 이벤트 발송 중...${NC}"

    # 작가 등록 테스트
    echo -e "${BLUE}👤 작가 등록 테스트${NC}"
    curl -X POST "http://localhost:8088/authors" \
        -H "Content-Type: application/json" \
        -d '{
            "authorId": 123,
            "name": "테스트 작가",
            "portfolio": "테스트 포트폴리오"
        }' || echo -e "${RED}작가 등록 실패${NC}"

    echo -e "\n${BLUE}📚 도서 등록 테스트${NC}"
    curl -X POST "http://localhost:8088/books" \
        -H "Content-Type: application/json" \
        -d '{
            "bookId": 424,
            "authorId": 123,
            "title": "테스트 도서",
            "content": "테스트 내용",
            "category": "소설"
        }' || echo -e "${RED}도서 등록 실패${NC}"

    echo -e "\n${BLUE}👥 구독 테스트${NC}"
    curl -X POST "http://localhost:8088/subscriptions" \
        -H "Content-Type: application/json" \
        -d '{
            "subscriptionId": 444,
            "userId": 9888,
            "startDate": "2024-01-01",
            "endDate": "2024-12-31",
            "isSubscription": true
        }' || echo -e "${RED}구독 등록 실패${NC}"
fi

echo -e "\n${GREEN}🎉 Kafka 통신 테스트 완료!${NC}"
echo "=========================================="
echo -e "${BLUE}💡 추가 모니터링 명령어:${NC}"
echo "- Kafka 로그 확인: docker logs $KAFKA_CONTAINER"
echo "- 토픽 메시지 확인: docker exec -it $KAFKA_CONTAINER kafka-console-consumer --bootstrap-server localhost:9092 --topic ktaivleminitocode --from-beginning"
