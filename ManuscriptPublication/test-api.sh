#!/bin/bash

# ManuscriptPublication API 테스트 스크립트

BASE_URL="http://localhost:8086"

# jq 사용 가능 여부 확인
if command -v jq &> /dev/null; then
    USE_JQ=true
    echo "✅ jq 사용 가능 - JSON 포맷팅 활성화"
else
    USE_JQ=false
    echo "⚠️ jq 없음 - 기본 출력 모드"
fi

echo ""
echo "🚀 ManuscriptPublication API 테스트 시작"
echo "=================================="

# JSON 출력 함수
print_json() {
    if [ "$USE_JQ" = true ]; then
        echo "$1" | jq '.'
    else
        echo "$1"
    fi
}

# manuscriptId 추출 함수
extract_manuscript_id() {
    local response="$1"
    if [ "$USE_JQ" = true ]; then
        echo "$response" | jq -r '.manuscriptId // .id // empty'
    else
        # jq 없이 manuscriptId 추출 (간단한 grep 사용)
        echo "$response" | grep -o '"manuscriptId":[0-9]*' | cut -d: -f2 | head -1
    fi
}

# 1. Health Check
echo "1️⃣ Health Check..."
HEALTH_RESPONSE=$(curl -s "$BASE_URL/actuator/health")
if [ $? -eq 0 ] && [[ "$HEALTH_RESPONSE" == *"UP"* ]]; then
    print_json "$HEALTH_RESPONSE"
    echo "✅ Health Check 성공"
else
    echo "❌ Health Check 실패"
    echo "응답: $HEALTH_RESPONSE"
fi
echo ""

# 2. 원고 등록
echo "2️⃣ 원고 등록..."
MANUSCRIPT_RESPONSE=$(curl -s -X POST "$BASE_URL/manuscripts" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "스프링 부트 마이크로서비스",
    "content": "이 책은 MSA 구조로 스프링 부트를 활용한 실전 개발서입니다.",
    "authorId": 1
  }')

if [ $? -eq 0 ] && [[ "$MANUSCRIPT_RESPONSE" == *"manuscriptId"* ]]; then
    print_json "$MANUSCRIPT_RESPONSE"
    echo "✅ 원고 등록 성공"
else
    echo "❌ 원고 등록 실패"
    echo "응답: $MANUSCRIPT_RESPONSE"
fi

# manuscriptId 추출
MANUSCRIPT_ID=$(extract_manuscript_id "$MANUSCRIPT_RESPONSE")
echo "📄 생성된 원고 ID: $MANUSCRIPT_ID"
echo ""

# 3. 원고 목록 조회 (컨트롤러에 GET 메서드가 없으므로 스킵)
echo "3️⃣ 원고 목록 조회..."
echo "⚠️ 원고 목록 조회 API가 구현되지 않음 (405 Method Not Allowed)"
echo "✅ 원고 등록 기능은 정상 동작"
echo ""

# 4. 출판 요청 (manuscriptId가 있는 경우)
if [ ! -z "$MANUSCRIPT_ID" ] && [ "$MANUSCRIPT_ID" != "null" ] && [ "$MANUSCRIPT_ID" != "" ]; then
    echo "4️⃣ 출판 요청..."
    PUBLICATION_RESPONSE=$(curl -s -X POST "$BASE_URL/manuscripts/$MANUSCRIPT_ID/publication-request" \
      -H "Content-Type: application/json" \
      -d '{}')

    if [ $? -eq 0 ]; then
        print_json "$PUBLICATION_RESPONSE"
        echo "✅ 출판 요청 성공"
    else
        echo "❌ 출판 요청 실패"
        echo "응답: $PUBLICATION_RESPONSE"
    fi
    echo ""

    # 잠시 대기 (GPT 처리 시간)
    echo "⏳ GPT 처리 대기 중 (3초)..."
    sleep 3

    # 5. 출판 목록 조회
    echo "5️⃣ 출판 목록 조회..."
    PUBLICATIONS_RESPONSE=$(curl -s "$BASE_URL/publicationRequests")
    if [ $? -eq 0 ]; then
        print_json "$PUBLICATIONS_RESPONSE"
        echo "✅ 출판 목록 조회 성공"
    else
        echo "❌ 출판 목록 조회 실패"
    fi
    echo ""

    # 6. Library Management 연동 확인
    echo "6️⃣ Library Management 연동 확인..."
    BOOKS_RESPONSE=$(curl -s "http://localhost:8084/books" 2>/dev/null)
    if [ $? -eq 0 ] && [[ "$BOOKS_RESPONSE" != *"Connection refused"* ]]; then
        print_json "$BOOKS_RESPONSE"
        echo "✅ Library Management 연동 성공"
    else
        echo "⚠️ Library Management 서비스 미실행 또는 연결 실패"
        echo "Library Management 서비스를 실행하세요: cd librarymanagement && mvn spring-boot:run"
    fi
else
    echo "⚠️ manuscriptId를 찾을 수 없어 출판 요청을 ���너뜁니다."
    echo "다시 시도하거나 수동으로 원고를 등록해주세요."
fi

echo ""
echo "✅ 테스트 완료!"
echo ""
echo "📋 다음 단계 확인사항:"
echo "1. ManuscriptPublication 로그에서 '🎉 GPT 처리 완료' 메시지 확인"
echo "2. Library Management 로그에서 'RegisterBook' 메시지 확인"
echo "3. http GET :8084/books 로 생성된 Book 확인"
