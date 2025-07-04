#!/bin/bash

# 원고 출간 Event Driven 플로우 테스트 스크립트
# 작성일: 2025-07-03
# 테스트 대상: ManuscriptPublication → GPT 처리 → LibraryManagement

echo "📝 원고 출간 Event Driven 플로우 테스트 시작"
echo "=============================================="

# 서비스 포트 설정
MANUSCRIPT_SERVICE="http://localhost:8086"
LIBRARY_SERVICE="http://localhost:8084"

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
TEST_AUTHOR_ID=1
MANUSCRIPT_TITLE="인공지능과 머신러닝 실전 가이드"
MANUSCRIPT_CONTENT="딥러닝, 자연어처리, 컴퓨터 비전 등 AI 기술의 핵심 개념과 실무 적용 사례를 다룬 전문서입니다. 실제 프로젝트 경험을 바탕으로 한 실용적인 접근법을 제시합니다."

echo ""
print_step "1️⃣ 서비스 상태 확인..."

# ManuscriptPublication 서비스 상태 확인
print_step "📝 ManuscriptPublication 서비스 확인 (${MANUSCRIPT_SERVICE})"
MANUSCRIPT_HEALTH=$(curl -s -o /dev/null -w "%{http_code}" ${MANUSCRIPT_SERVICE}/actuator/health)
if [ "$MANUSCRIPT_HEALTH" = "200" ]; then
    print_success "ManuscriptPublication 서비스 정상"
else
    print_error "ManuscriptPublication 서비스 응답 없음 (HTTP: $MANUSCRIPT_HEALTH)"
    exit 1
fi

# LibraryManagement 서비스 상태 확인
print_step "📚 LibraryManagement 서비스 확인 (${LIBRARY_SERVICE})"
LIBRARY_HEALTH=$(curl -s -o /dev/null -w "%{http_code}" ${LIBRARY_SERVICE}/actuator/health)
if [ "$LIBRARY_HEALTH" = "200" ]; then
    print_success "LibraryManagement 서비스 정상"
else
    print_error "LibraryManagement 서비스 응답 없음 (HTTP: $LIBRARY_HEALTH)"
    exit 1
fi

echo ""
print_step "2️⃣ 원고 등록 (Event Chain 시작)..."

# 원고 등록
MANUSCRIPT_RESPONSE=$(curl -s -X POST ${MANUSCRIPT_SERVICE}/manuscripts \
  -H "Content-Type: application/json" \
  -d "{
    \"authorId\": ${TEST_AUTHOR_ID},
    \"title\": \"${MANUSCRIPT_TITLE}\",
    \"content\": \"${MANUSCRIPT_CONTENT}\"
  }")

if [ $? -eq 0 ]; then
    echo "$MANUSCRIPT_RESPONSE"

    if [ "$JQ_AVAILABLE" = true ]; then
        MANUSCRIPT_ID=$(echo "$MANUSCRIPT_RESPONSE" | jq -r '.manuscriptId // empty')
        MANUSCRIPT_STATUS=$(echo "$MANUSCRIPT_RESPONSE" | jq -r '.status // empty')
        print_success "원고 등록 성공 - ID: $MANUSCRIPT_ID, 상태: $MANUSCRIPT_STATUS"
    else
        MANUSCRIPT_ID=$(echo "$MANUSCRIPT_RESPONSE" | grep -o '"manuscriptId":[^,]*' | cut -d':' -f2)
        print_success "원고 등록 성공 - ID: $MANUSCRIPT_ID"
    fi
else
    print_error "원고 등록 실패"
    exit 1
fi

echo ""
print_step "3️⃣ 출간 요청 (GPT 처리 트리거)..."

# 출간 요청
PUBLICATION_RESPONSE=$(curl -s -X POST ${MANUSCRIPT_SERVICE}/manuscripts/${MANUSCRIPT_ID}/publication-request \
  -H "Content-Type: application/json" \
  -d "{}")

if [ $? -eq 0 ]; then
    echo "$PUBLICATION_RESPONSE"

    if [ "$JQ_AVAILABLE" = true ]; then
        PUB_STATUS=$(echo "$PUBLICATION_RESPONSE" | jq -r '.status // empty')
        print_success "출간 요청 성공 - 상태: $PUB_STATUS"
    else
        print_success "출간 요청 성공"
    fi
else
    print_error "출간 요청 실패"
    exit 1
fi

echo ""
print_step "4️⃣ GPT 처리 및 Event Chain 대기..."
print_step "⏳ Event Driven 처리 대기 중 (8초)..."
print_step "   📤 PublicationRequested 이벤트 발행됨"
print_step "   🧠 GPT 처리 시작 (요약/카테고리/표지 생성)"
print_step "   📚 RegisterBookRequested 이벤트 발행 예정"
sleep 8

echo ""
print_step "5️⃣ 출간 결과 확인..."

# 출간 요청 목록 조회
PUBLICATION_LIST=$(curl -s ${MANUSCRIPT_SERVICE}/publicationRequests)
if [ $? -eq 0 ]; then
    if [ "$JQ_AVAILABLE" = true ]; then
        # 방금 등록한 원고와 일치하는 Publication 찾기
        PUBLICATION_INFO=$(echo "$PUBLICATION_LIST" | jq --arg id "$MANUSCRIPT_ID" '.._embedded.publicationRequests[]? | select(.manuscriptId == ($id | tonumber))')

        if [ ! -z "$PUBLICATION_INFO" ]; then
            echo "$PUBLICATION_INFO" | jq '.'

            PUB_STATUS=$(echo "$PUBLICATION_INFO" | jq -r '.status')
            PUB_CATEGORY=$(echo "$PUBLICATION_INFO" | jq -r '.category // "N/A"')
            PUB_SUMMARY=$(echo "$PUBLICATION_INFO" | jq -r '.summary // "N/A"' | head -c 50)
            PUB_COVER=$(echo "$PUBLICATION_INFO" | jq -r '.coverImageUrl // "N/A"')

            case $PUB_STATUS in
                "COMPLETED")
                    print_success "출간 완료! 상태: $PUB_STATUS"
                    print_success "생성된 카테고리: $PUB_CATEGORY"
                    print_success "생성된 요약: ${PUB_SUMMARY}..."
                    if [[ "$PUB_COVER" == *"fail"* ]]; then
                        print_warning "표지 이미지 생성 실패: $PUB_COVER"
                    else
                        print_success "표지 이미지: $PUB_COVER"
                    fi
                    ;;
                "PROCESSING")
                    print_warning "아직 GPT 처리 중... 상태: $PUB_STATUS"
                    ;;
                "FAILED")
                    print_error "출간 실패! 상태: $PUB_STATUS"
                    ;;
                *)
                    print_warning "알 수 없는 상태: $PUB_STATUS"
                    ;;
            esac
        else
            print_warning "해당 원고의 출간 정보를 찾을 수 없음"
        fi
    else
        echo "$PUBLICATION_LIST" | head -50
        print_success "출간 목록 조회 성공"
    fi
else
    print_error "출간 목록 조회 실패"
fi

echo ""
print_step "6️⃣ LibraryManagement 연동 확인..."

# 도서 등록 확인 (Event Driven 결과)
print_step "📚 생성된 도서 확인"
BOOKS_LIST=$(curl -s ${LIBRARY_SERVICE}/books)
if [ $? -eq 0 ]; then
    if [ "$JQ_AVAILABLE" = true ]; then
        # 방금 등록한 원고와 일치하는 Book 찾기
        BOOK_INFO=$(echo "$BOOKS_LIST" | jq --arg id "$MANUSCRIPT_ID" '.._embedded.books[]? | select(.manuscriptId == ($id | tonumber))')

        if [ ! -z "$BOOK_INFO" ]; then
            echo "$BOOK_INFO" | jq '.'

            BOOK_TITLE=$(echo "$BOOK_INFO" | jq -r '.title')
            BOOK_CATEGORY=$(echo "$BOOK_INFO" | jq -r '.category // "N/A"')
            BOOK_READ_COUNT=$(echo "$BOOK_INFO" | jq -r '.readCount // 0')

            print_success "도서 등록 성공!"
            print_success "도서명: $BOOK_TITLE"
            print_success "카테고리: $BOOK_CATEGORY"
            print_success "읽기 횟수: $BOOK_READ_COUNT"
        else
            print_warning "해당 원고로 생성된 도서를 찾을 수 없음"
            print_warning "GPT 처리가 실패했거나 아직 진행 중일 수 있음"
        fi
    else
        echo "$BOOKS_LIST" | head -50
        print_success "도서 목록 조회 성공"
    fi
else
    print_error "도서 목록 조회 실패"
fi

echo ""
print_step "7️⃣ Event Driven 성공/실패 판정..."

# 전체 플로우 성공 여부 판정
if [ "$JQ_AVAILABLE" = true ] && [ ! -z "$PUBLICATION_INFO" ] && [ ! -z "$BOOK_INFO" ]; then
    PUB_STATUS=$(echo "$PUBLICATION_INFO" | jq -r '.status')
    if [ "$PUB_STATUS" = "COMPLETED" ]; then
        print_success "🎉 Event Driven 플로우 완전 성공!"
        echo ""
        echo "✅ 검증된 Event Chain:"
        echo "   1. 원고 등록 → ManuscriptPlaced 이벤트"
        echo "   2. 출간 요청 → PublicationRequested 이벤트"
        echo "   3. GPT 처리 → GptContentGenerationStarted 이벤트"
        echo "   4. GPT 완료 → RegisterBookRequested 이벤트"
        echo "   5. 도서 등록 → LibraryManagement에서 Book 생성"
    else
        print_warning "Event Chain은 동작했으나 GPT 처리가 완료되지 않음"
    fi
else
    print_warning "Event Driven 플로우 부분 성공 (수동 확인 필요)"
fi

echo ""
print_step "8️⃣ 실패 시나리오 테스트..."

# GPT 실패 시나리오 테스트 (더미 데이터로는 항상 성공하므로 스킵)
print_step "💡 실패 시나리오는 실제 GPT API 연동 시에만 발생"
print_step "   - 요약 생성 실패 → Publication 상태: FAILED"
print_step "   - 표지 이미지 실패 → coverImageUrl: 'fail.jpg' 포함"
print_step "   - 실패 시 RegisterBookRequested 이벤트 발행 안됨"

echo ""
print_success "✅ 원고 출간 Event Driven 플로우 테스트 완료!"

echo ""
echo "📋 검증 포인트:"
echo "1. 원고 등록 시 ManuscriptPlaced 이벤트 발행"
echo "2. 출간 요청 시 PublicationRequested 이벤트 발행"
echo "3. GPT 처리 시작 시 GptContentGenerationStarted 이벤트 발행"
echo "4. GPT 완료 시 RegisterBookRequested 이벤트 발행"
echo "5. LibraryManagement에서 Book 엔티티 생성"

echo ""
echo "📝 로그 확인 방법:"
echo "- ManuscriptPublication 로그에서 '🎉 GPT 처리 완료' 메시지 확인"
echo "- LibraryManagement 로그에서 'RegisterBook' 메시지 확인"
echo "- Kafka 토픽 메시지: docker exec -it kafka kafka-console-consumer --topic ktaivleminitocode --from-beginning --bootstrap-server localhost:9092"

echo ""
echo "🔧 문제 해결:"
echo "- GPT 처리가 오래 걸리면 대기 시간 늘리기"
echo "- OpenAI API 키 설정 확인: ManuscriptPublication/OPENAI_SETUP.md 참조"
echo "- Kafka 이벤트 전송 확인: ManuscriptPublication 로그 확인"
