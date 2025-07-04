#!/bin/bash

# 전체 서비스 헬스체크 및 상태 모니터링 스크립트

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

echo -e "${PURPLE}🏥 마이크로서비스 헬스체크 대시보드${NC}"
echo "=========================================="

# 현재 시간 표시
echo -e "${BLUE}📅 검사 시간: $(date '+%Y-%m-%d %H:%M:%S')${NC}"
echo ""

# 1. 인프라 상태 확인
echo -e "${YELLOW}🏗️ 인프라 상태 확인${NC}"
echo "----------------------------------------"

# Docker 상태 확인
if command -v docker &> /dev/null && docker ps &> /dev/null; then
    echo -e "${GREEN}✅ Docker 실행 중${NC}"

    # Kafka 상태 확인
    if docker ps | grep -q kafka; then
        kafka_container=$(docker ps | grep kafka | awk '{print $1}')
        echo -e "${GREEN}✅ Kafka 컨테이너 실행 중: $kafka_container${NC}"

        # Kafka 토픽 확인
        topics=$(docker exec $kafka_container kafka-topics --list --bootstrap-server localhost:9092 2>/dev/null | wc -l)
        echo -e "${BLUE}📋 활성 토픽 수: $topics${NC}"
    else
        echo -e "${RED}❌ Kafka 컨테이너 미실행${NC}"
    fi
else
    echo -e "${RED}❌ Docker 미실행 또는 접근 불가${NC}"
fi

# 2. 마이크로서비스 상태 확인
echo ""
echo -e "${YELLOW}🔍 마이크로서비스 상태 확인${NC}"
echo "----------------------------------------"

declare -A services=(
    ["Subscription Management"]="8080"
    ["Author Management"]="8082"
    ["Library Management"]="8084"
    ["Point Management"]="8085"
    ["Manuscript Publication"]="8086"
    ["Gateway"]="8088"
)

total_services=${#services[@]}
healthy_services=0

for service_name in "${!services[@]}"; do
    port="${services[$service_name]}"
    echo -n -e "${BLUE}🔍 $service_name (포트: $port)${NC} ... "

    # 헬스체크 엔드포인트 확인
    if timeout 5 curl -s -f "http://localhost:$port/actuator/health" > /dev/null 2>&1; then
        # 상세 헬스 정보 가져오기
        health_response=$(curl -s "http://localhost:$port/actuator/health" 2>/dev/null)
        status=$(echo $health_response | grep -o '"status":"[^"]*"' | cut -d'"' -f4 2>/dev/null || echo "UNKNOWN")

        if [ "$status" = "UP" ]; then
            echo -e "${GREEN}✅ 정상 ($status)${NC}"
            ((healthy_services++))
        else
            echo -e "${YELLOW}⚠️ 경고 ($status)${NC}"
        fi
    else
        echo -e "${RED}❌ 연결 실패${NC}"
    fi
done

# 3. 전체 시스템 상태 요약
echo ""
echo -e "${YELLOW}📊 시스템 상태 요약${NC}"
echo "----------------------------------------"
echo -e "${BLUE}전체 서비스: $total_services${NC}"
echo -e "${GREEN}정상 서비스: $healthy_services${NC}"
echo -e "${RED}문제 서비스: $((total_services - healthy_services))${NC}"

# 상태에 따른 전체 평가
if [ $healthy_services -eq $total_services ]; then
    echo -e "${GREEN}🎉 모든 서비스가 정상 동작 중입니다!${NC}"
elif [ $healthy_services -gt $((total_services / 2)) ]; then
    echo -e "${YELLOW}⚠️ 일부 서비스에 문제가 있습니다.${NC}"
else
    echo -e "${RED}🚨 시스템에 심각한 문제가 있습니다!${NC}"
fi

# 4. API 엔드포인트 테스트
echo ""
echo -e "${YELLOW}🧪 API 엔드포인트 테스트${NC}"
echo "----------------------------------------"

# Gateway를 통한 각 서비스 접근 테스트
if curl -s -f "http://localhost:8088/actuator/health" > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Gateway 접근 가능${NC}"

    # 각 서비스별 기본 엔드포인트 테스트
    endpoints=(
        "/authors:Author Management"
        "/subscriptions:Subscription Management"
        "/books:Library Management"
        "/users:Point Management"
    )

    for endpoint_info in "${endpoints[@]}"; do
        IFS=':' read -r endpoint service_name <<< "$endpoint_info"
        echo -n -e "${BLUE}🔗 $service_name ($endpoint)${NC} ... "

        if timeout 3 curl -s -f "http://localhost:8088$endpoint" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ 접근 가능${NC}"
        else
            echo -e "${YELLOW}⚠️ 접근 불가 (정상적일 수 있음)${NC}"
        fi
    done
else
    echo -e "${RED}❌ Gateway 접근 불가 - API 테스트 건너뜀${NC}"
fi

# 5. 리소스 사용량 확인
echo ""
echo -e "${YELLOW}💻 리소스 사용량${NC}"
echo "----------------------------------------"

if command -v docker &> /dev/null && docker ps &> /dev/null; then
    echo -e "${BLUE}🐳 Docker 컨테이너 리소스:${NC}"
    docker stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}" | head -10
fi

# 6. 권장 액션
echo ""
echo -e "${YELLOW}💡 권장 액션${NC}"
echo "----------------------------------------"

if [ $healthy_services -lt $total_services ]; then
    echo -e "${BLUE}🔧 문제 해결 가이드:${NC}"
    echo "1. 각 서비스 로그 확인: docker-compose logs [서비스명]"
    echo "2. Kafka 상태 확인: ./test-kafka-communication.sh"
    echo "3. 서비스 재시작: docker-compose restart [서비스명]"
    echo "4. 전체 재시작: docker-compose down && docker-compose up -d"
fi

echo -e "${BLUE}🚀 배포 가이드:${NC}"
echo "1. Docker 이미지 빌드: ./build-docker-images.sh"
echo "2. Azure 배포: ./deploy-to-azure.sh"
echo "3. Kafka 통신 테스트: ./test-kafka-communication.sh"

echo ""
echo -e "${PURPLE}📊 헬스체크 완료!${NC}"
echo "=========================================="
