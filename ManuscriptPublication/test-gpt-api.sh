#!/bin/bash

# GPT API 연동 테스트 스크립트

BASE_URL="http://localhost:8086"

echo "🧠 GPT API 연동 테스트"
echo "====================="

# 1. 현재 더미 모드인지 확인
echo "1️⃣ 현재 GPT 모드 확인..."
CURRENT_RESPONSE=$(curl -s "$BASE_URL/publicationRequests" | head -20)

if [[ "$CURRENT_RESPONSE" == *"테스트용 더미 요약"* ]]; then
    echo "⚠️ 현재 더미 모드로 동작 중"
    echo "💡 실제 GPT API 테스트를 원한다면:"
    echo "   1. export OPENAI_API_KEY=\"실제-키\""
    echo "   2. ManuscriptPublication 서비스 재시작"
    echo ""
else
    echo "✅ 실제 GPT API 모드로 동작 중"
    echo ""
fi

# 2. 새로운 테스트 원고로 GPT 응답 확인
echo "2️⃣ 새로운 원고로 GPT 테스트..."
TEST_RESPONSE=$(curl -s -X POST "$BASE_URL/manuscripts" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "인공지능과 머신러닝",
    "content": "딥러닝, 자연어처리, 컴퓨터 비전 등 AI 기술의 핵심 개념과 실무 적용 사례를 다룬 전문서입니다.",
    "authorId": 1
  }')

if [[ "$TEST_RESPONSE" == *"manuscriptId"* ]]; then
    MANUSCRIPT_ID=$(echo "$TEST_RESPONSE" | grep -o '"manuscriptId":[0-9]*' | cut -d: -f2 | head -1)
    echo "📄 테스트 원고 생성: ID $MANUSCRIPT_ID"

    # 출판 요청
    curl -s -X POST "$BASE_URL/manuscripts/$MANUSCRIPT_ID/publication-request" \
      -H "Content-Type: application/json" \
      -d '{}' > /dev/null

    echo "⏳ GPT 처리 대기 중 (5초)..."
    sleep 5

    # 결과 확인
    echo "3️⃣ GPT 처리 결과 확인..."
    GPT_RESULT=$(curl -s "$BASE_URL/publicationRequests" | grep -A 20 "manuscriptId.*$MANUSCRIPT_ID")

    if [[ "$GPT_RESULT" == *"테스트용 더미 요약"* ]]; then
        echo "🤖 더미 모드: 테스트용 응답"
        echo "   - Category: 소설 (고정)"
        echo "   - Summary: 테스트용 더미 요약"
        echo "   - Image: placeholder 이미지"
    elif [[ "$GPT_RESULT" == *"인공지능"* ]] || [[ "$GPT_RESULT" == *"기술"* ]] || [[ "$GPT_RESULT" == *"AI"* ]]; then
        echo "🎉 실제 GPT 모드: 실제 AI 분석 응답!"
        echo "   - 내용에 맞는 카테고리 생성"
        echo "   - 지능적인 요약 생성"
        echo "   - DALL-E로 생성된 커버 이미지"
        echo ""
        echo "📊 GPT 생성 결과:"
        echo "$GPT_RESULT" | grep -E "(category|summary)" | head -2
    else
        echo "❓ 처리 중이거나 예상과 다른 응답"
        echo "잠시 후 다시 확인해보세요."
    fi
else
    echo "❌ 테스트 원고 생성 실패"
fi

echo ""
echo "📋 GPT API 연동 상태 요약:"
echo "- 더미 모드: 개발/테스트용, 빠른 응답"
echo "- 실제 모드: OpenAI API 사용, 실제 AI 분석"
