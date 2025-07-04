#!/bin/bash

# 도서 열람 Event Driven 플로우 테스트 스크립트
# 작성일: 2025-07-03
# 테스트 대상: PointManagement → SubscriptionManagement → LibraryManagement

echo "📚 도서 열람 Event Driven 플로우 테스트 시작"
echo "=================================================="

# 서비스 포트 설정 (실제 환경에 맞게 수정)
POINT_SERVICE="http://localhost:8082"
SUBSCRIPTION_SERVICE="http://localhost:8083"
LIBRARY_SERVICE="http://localhost:8084"
MANUSCRIPT_SERVICE="http://localhost:8086"

# jq 설치 확인
if ! command -v jq &> /dev/null; then
    echo "⚠️ jq 없음 - 기본 출력 모드"
    JQ_AVAILABLE=false
else
    echo "✅ jq 사용 가능"
    JQ_AVAILABLE=true
fi

# 색상 코드 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_step() {
    echo -e "${BLUE}$1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# 테스트 데이터
TEST_USER_ID=1
TEST_BOOK_ID=153
POINT_COST=100

echo ""
print_step "1️⃣ 서비스 상태 확인..."

# PointManagement 서비스 상태 확인
print_step "📊 PointManagement 서비스 확인 (${POINT_SERVICE})"
POINT_HEALTH=$(curl -s -o /dev/null -w "%{http_code}" ${POINT_SERVICE}/actuator/health)
if [ "$POINT_HEALTH" = "200" ]; then
    print_success "PointManagement 서비스 정상"
else
    print_error "PointManagement 서비스 응답 없음 (HTTP: $POINT_HEALTH)"
fi

# SubscriptionManagement 서비스 상태 확인
print_step "📋 SubscriptionManagement 서비스 확인 (${SUBSCRIPTION_SERVICE})"
SUB_HEALTH=$(curl -s -o /dev/null -w "%{http_code}" ${SUBSCRIPTION_SERVICE}/actuator/health)
if [ "$SUB_HEALTH" = "200" ]; then
    print_success "SubscriptionManagement 서비스 정상"
else
    print_error "SubscriptionManagement 서비스 응답 없음 (HTTP: $SUB_HEALTH)"
fi

# LibraryManagement 서비스 상태 확인
print_step "📚 LibraryManagement 서비스 확인 (${LIBRARY_SERVICE})"
LIBRARY_HEALTH=$(curl -s -o /dev/null -w "%{http_code}" ${LIBRARY_SERVICE}/actuator/health)
if [ "$LIBRARY_HEALTH" = "200" ]; then
    print_success "LibraryManagement 서비스 정상"
else
    print_error "LibraryManagement 서비스 응답 없음 (HTTP: $LIBRARY_HEALTH)"
fi

echo ""
print_step "2️⃣ 테스트 사용자 생성/확인..."

# 사용자 등록 (포인트 지급 포함)
print_step "👤 사용자 등록 (초기 포인트 지급)"
USER_REGISTRATION_RESPONSE=$(curl -s -X POST ${POINT_SERVICE}/users/userregistration \
  -H "Content-Type: application/json" \
  -d "{
    \"userId\": ${TEST_USER_ID},
    \"name\": \"테스트사용자\",
    \"ktCustomer\": true
  }")

if [ $? -eq 0 ]; then
    echo "$USER_REGISTRATION_RESPONSE"
    print_success "사용자 등록 완료"
else
    print_warning "사용자 등록 실패 (이미 존재할 수 있음)"
fi

echo ""
print_step "3️⃣ 구독 정보 설정..."

# 구독 요청
print_step "📝 구독 요청"
SUBSCRIPTION_RESPONSE=$(curl -s -X POST ${SUBSCRIPTION_SERVICE}/subscriptions/requestsubscription \
  -H "Content-Type: application/json" \
  -d "{
    \"userId\": ${TEST_USER_ID},
    \"startDate\": \"2025-01-01T00:00:00.000Z\",
    \"endDate\": \"2025-12-31T23:59:59.000Z\"
  }")

if [ $? -eq 0 ]; then
    echo "$SUBSCRIPTION_RESPONSE"
    print_success "구독 요청 완료"
else
    print_warning "구독 요청 실패"
fi

echo ""
print_step "4️⃣ 사용 가능한 도서 목록 확인..."

# 도서 목록 조회
BOOKS_RESPONSE=$(curl -s ${LIBRARY_SERVICE}/books)
if [ $? -eq 0 ]; then
    if [ "$JQ_AVAILABLE" = true ]; then
        echo "$BOOKS_RESPONSE" | jq '.._embedded.books[0:3] | .[] | {bookId: .bookId, title: .title, readCount: .readCount}'
    else
        echo "$BOOKS_RESPONSE" | head -20
    fi
    print_success "도서 목록 조회 성공"
else
    print_error "도서 목록 조회 실패"
fi

echo ""
print_step "5️⃣ 도서 열람 요청 (Event Driven 플로우 시작)..."

# 현재 사용자 포인트 확인
print_step "💰 현재 사용자 포인트 확인"
USER_INFO=$(curl -s ${POINT_SERVICE}/users/${TEST_USER_ID})
if [ $? -eq 0 ]; then
    echo "$USER_INFO"
    if [ "$JQ_AVAILABLE" = true ]; then
        CURRENT_POINTS=$(echo "$USER_INFO" | jq -r '.points // 0')
        print_success "현재 포인트: $CURRENT_POINTS"
    fi
else
    print_warning "사용자 정보 조회 실패"
fi

echo ""
print_step "📖 도서 열람 요청 실행"
READ_BOOK_RESPONSE=$(curl -s -X POST ${POINT_SERVICE}/users/placereadbook \
  -H "Content-Type: application/json" \
  -d "{
    \"userId\": ${TEST_USER_ID},
    \"bookId\": ${TEST_BOOK_ID},
    \"pointCost\": ${POINT_COST}
  }")

if [ $? -eq 0 ]; then
    echo "$READ_BOOK_RESPONSE"
    print_success "도서 열람 요청 완료"

    if [ "$JQ_AVAILABLE" = true ]; then
        NEW_POINTS=$(echo "$READ_BOOK_RESPONSE" | jq -r '.points // "N/A"')
        print_success "포인트 차감 후: $NEW_POINTS"
    fi
else
    print_error "도서 열람 요청 실패"
fi

echo ""
print_step "6️⃣ Event Driven 처리 대기..."
print_step "⏳ Kafka 이벤트 처리 대기 중 (5초)..."
sleep 5

echo ""
print_step "7️⃣ 처리 결과 확인..."

# 도서 읽기 횟수 증가 확인
print_step "📊 도서 읽기 횟수 확인"
BOOK_INFO=$(curl -s ${LIBRARY_SERVICE}/books/${TEST_BOOK_ID})
if [ $? -eq 0 ]; then
    echo "$BOOK_INFO"
    if [ "$JQ_AVAILABLE" = true ]; then
        READ_COUNT=$(echo "$BOOK_INFO" | jq -r '.readCount // 0')
        print_success "도서 읽기 횟수: $READ_COUNT"
    fi
else
    print_warning "도서 정보 조회 실패"
fi

echo ""
print_step "8️⃣ 사용자 최종 상태 확인..."

# 사용자 최종 포인트 확인
USER_FINAL_INFO=$(curl -s ${POINT_SERVICE}/users/${TEST_USER_ID})
if [ $? -eq 0 ]; then
    echo "$USER_FINAL_INFO"
    if [ "$JQ_AVAILABLE" = true ]; then
        FINAL_POINTS=$(echo "$USER_FINAL_INFO" | jq -r '.points // 0')
        print_success "최종 포인트: $FINAL_POINTS"
    fi
else
    print_warning "사용자 최종 정보 조회 실패"
fi

echo ""
print_step "9️⃣ 포인트 부족 시나리오 테스트..."

# 포인트를 모두 소진시켜서 실패 시나리오 테스트
print_step "💸 포인트 부족 상황에서 도서 열람 요청"
FAIL_READ_RESPONSE=$(curl -s -X POST ${POINT_SERVICE}/users/placereadbook \
  -H "Content-Type: application/json" \
  -d "{
    \"userId\": ${TEST_USER_ID},
    \"bookId\": ${TEST_BOOK_ID},
    \"pointCost\": 999999
  }")

if [ $? -eq 0 ]; then
    echo "$FAIL_READ_RESPONSE"
    print_success "포인트 부족 시나리오 테스트 완료"
else
    print_error "포인트 부족 시나리오 테스트 실패"
fi

echo ""
print_success "✅ 도서 열람 Event Driven 플로우 테스트 완료!"

echo ""
echo "📋 검증 포인트:"
echo "1. 포인트 차감이 정상적으로 이루어졌는지 확인"
echo "2. ReadBookPlaced 이벤트가 발행되었는지 확인"
echo "3. SubscriptionChecked 이벤트가 발행되었는지 확인"
echo "4. 도서 읽기 횟수가 증가했는지 확인"
echo "5. 포인트 부족 시 BookReadFailed 이벤트가 발행되었는지 확인"

echo ""
echo "📝 로그 확인 방법:"
echo "- PointManagement 로그에서 'ReadBookPlaced' 또는 'BookReadFailed' 메시지 확인"
echo "- SubscriptionManagement 로그에서 'CheckSubscription' 메시지 확인"
echo "- LibraryManagement 로그에서 'ReadBook' 메시지 확인"

echo ""
echo "🔧 문제 해결:"
echo "- 서비스가 응답하지 않으면 각 서비스가 실행 중인지 확인"
echo "- Kafka가 실행 중인지 확인: docker-compose -f infra/docker-compose.yml ps"
echo "- 포트 충돌이 있는지 확인"
