# 📡 ManuscriptPublication API 테스트 가이드

> **완벽한 HTTP 통신 및 이벤트 플로우 테스트 방법 모음**

## 🚀 빠른 시작

### 1. 서버 실행
```bash
# 1. Kafka 먼저 실행 (필수)
cd infra && docker-compose up -d

# 2. ManuscriptPublication 서비스
cd ../ManuscriptPublication && mvn spring-boot:run

# 3. Library Management 서비스 (새 터미널)
cd ../librarymanagement && mvn spring-boot:run
```

### 2. 서비스 상태 확인
```bash
# ManuscriptPublication (8086)
curl http://localhost:8086/actuator/health

# Library Management (8084)  
curl http://localhost:8084/actuator/health
```

---

## 🏆 최고의 HTTP 테스트 방법들

### 🥇 **방법 1: 자동화 스크립트** (���장 추천!)

전체 이벤트 플로우를 자동으로 테스트하는 스크립트입니다.

**실행 방법:**
```bash
# 실행 권한 부여 (처음 한 번만)
chmod +x test-api.sh

# 전체 API 테스트 자동 실행
./test-api.sh
```

**스크립트 장점:**
- ✅ 순차적으로 모든 API 테스트
- 📊 JSON 결과를 예쁘게 출력 (jq 사용)
- 🔗 이전 응답의 ID를 다음 요청에 자동 사용
- ✅ 성공/실패 상태 명���히 표시
- 🚀 원고 등록 → GPT 처리 → 도서 등록 전체 플로우 자동 테스트

**예상 출력:**
```
🚀 ManuscriptPublication API 테스트 시작
==================================
1️⃣ Health Check...
{
  "status": "UP"
}

2️⃣ 원고 등록...
{
  "manuscriptId": 1,
  "title": "스프링 부트 마이크로서비스",
  "authorId": 1,
  "status": "DRAFT"
}
📄 생성된 원고 ID: 1

4️⃣ 출판 요청...
{
  "publicationRequestId": 1,
  "status": "PROCESSING"
}

✅ 테스트 완료!
```

### 🥈 **방법 2: REST Client 파일** (IDE에서 편리!)

`api-test.http` 파일을 VSCode나 IntelliJ에서 바로 실행하세요.

**사용 방법:**
1. `api-test.http` 파일 열기
2. 각 요청 위의 `Send Request` 버튼 클릭
3. 결과가 바로 옆에 표시됨

**파일 예시:**
```http
### 1. 서비스 상태 확인
GET http://localhost:8086/actuator/health

### 2. 원고 등록
POST http://localhost:8086/manuscripts
Content-Type: application/json

{
  "title": "스프링 부트 마이크로서비스",
  "content": "이 책은 MSA 구조로 스프링 부트를 활용한 실전 개발서입니다.",
  "authorId": 1
}

### 3. 출판 요청
POST http://localhost:8086/manuscripts/1/requestPublication
Content-Type: application/json

{}
```

### 🥉 **방법 3: HTTPie** (기존 방식 개선)

기존에 사용하시던 방식을 올바른 포트로 수정했습니다.

```bash
# 원고 등록
http POST :8086/manuscripts title="스프링 부트 마이크로서비스" content="이 책은 MSA 구조로 스프링 부트를 활용한 실전 개발서입니다." authorId:=1

# 출판 요청 (manuscriptId=1인 경우)
http POST :8086/manuscripts/1/requestPublication

# 원고 목록 조회
http GET :8086/manuscripts

# 출판 목록 조회
http GET :8086/publications
```

### 📋 **방법 4: cURL** (표준적)

```bash
# 원고 등록
curl -X POST http://localhost:8086/manuscripts \
  -H "Content-Type: application/json" \
  -d '{
    "title": "스프링 부트 마이크로서비스",
    "content": "이 책은 MSA 구조로 스프링 부트를 활용한 실전 개발서입니다.",
    "authorId": 1
  }'

# 출판 요청
curl -X POST http://localhost:8086/manuscripts/1/requestPublication \
  -H "Content-Type: application/json" \
  -d '{}'
```

### 📱 **방법 5: Postman** (GUI 선호시)

`postman-collection.json` 파일을 Postman에 import해서 사용하세요.

**Import 방법:**
1. Postman 실행
2. File → Import 
3. `postman-collection.json` 선택
4. 컬렉션에서 요청들 실행

---

## 🔄 **완전한 이벤트 플로우 테스트**

### **전체 마이크로서비스 연동 테스트 시나리오:**

```bash
# 1. 원고 등록 (ManuscriptPublication)
curl -X POST http://localhost:8086/manuscripts \
  -H "Content-Type: application/json" \
  -d '{"title": "테스트 원고", "content": "테스트 내용", "authorId": 1}'

# 2. 출판 요청 (GPT 처리 + 이벤트 발행)
curl -X POST http://localhost:8086/manuscripts/1/requestPublication \
  -H "Content-Type: application/json" \
  -d '{}'

# 3. Library Management에서 등록된 도서 확인
curl http://localhost:8084/books

# 4. 특정 도서 상세 조회
curl http://localhost:8084/books/1
```

### **예상되는 이벤트 플로우:**
```
📝 Manuscript 생성
    ⬇️
📚 PublicationRequested 이벤트 발행
    ⬇️
🧠 GPT 처리 시작 (GptContentGenerationStarted)
    ⬇️
✨ GPT 처리 완료 (카테고리, 요약, 커버 이미지 생성)
    ⬇️
📘 RegisterBookRequested 이벤트 발행
    ⬇️
📖 Library Management에서 Book 생성
    ⬇️
🎉 BookPublished 이벤트 발행
```

---

## 📊 **출력 및 로그 확인 방법**

### **서버 로그에서 확인할 내용들:**

#### **ManuscriptPublication 서비스 로그:**
```log
✅ [EVENT] PublicationRequested : PublicationRequested{...}
📝 저장된 Publication ID = 1

🧠 [EVENT] GptContentGenerationStarted : GptContentGenerationStarted{...}
🎉 GPT 처리 완료 → category/summary/coverImageUrl 저장 및 이벤트 발행 완료
```

#### **Library Management 서비스 로그:**
```log
##### listener RegisterBook : RegisterBookRequested{
  publicationRequestId=1, 
  manuscriptId=1, 
  authorId=1, 
  title='스프링 부트 마이크로서비스',
  category='개발',
  summary='테스트용 더미 요약입니다...'
}
```

### **실시간 로그 모니터링:**
```bash
# ManuscriptPublication 로그 (새 터미널)
tail -f logs/manuscript-publication.log

# Library Management 로그 (새 터미널)
tail -f logs/library-management.log
```

### **Kafka 이벤트 모니터링:**
```bash
# Kafka 컨테이너 접속
docker exec -it kafka bash

# 이벤트 실시간 확인
kafka-console-consumer --bootstrap-server localhost:9092 --topic ktaivleminitocode --from-beginning
```

---

## 🔧 **문제 해결**

### **일반적인 문제들:**

#### **1. 서비스 연결 실패**
```bash
# 서비스 상태 확인
curl http://localhost:8086/actuator/health
curl http://localhost:8084/actuator/health

# 포트 사용 확인
netstat -tulpn | grep :8086
netstat -tulpn | grep :8084
```

#### **2. Kafka 연결 실패**
```bash
# Kafka 컨테이너 상태 확인
docker ps | grep kafka

# Kafka 재시작
cd infra && docker-compose restart
```

#### **3. OpenAI API 오류**
- 테스트 모드에서는 더미 응답이 나오므로 정상
- 실제 GPT 기능 테스트시에만 API Key 필요

### **디버깅 팁:**
```bash
# 상세한 로그 출력으로 서버 실행
mvn spring-boot:run -Dspring.profiles.active=debug

# JSON 응답 예쁘게 보기 (jq 설��� 필요)
curl http://localhost:8086/manuscripts | jq '.'

# HTTP 응답 헤더까지 확인
curl -v http://localhost:8086/manuscripts
```

---

## 📝 **테스트 체크리스트**

### **기본 기능 테스트:**
- [ ] Health Check 성공
- [ ] 원고 등록 성공
- [ ] 원고 목록 조회 성공
- [ ] 출판 요청 성공
- [ ] 출판 목록 조회 성공

### **이벤트 플로우 테스트:**
- [ ] PublicationRequested 이벤트 발행 확인
- [ ] GPT 처리 시작 로그 확인  
- [ ] RegisterBookRequested 이벤트 발행 확인
- [ ] Library Management에서 Book 생성 확인
- [ ] BookPublished 이벤트 발행 확인

### **마이크로서비스 연동 테스트:**
- [ ] ManuscriptPublication → Library Management 연동
- [ ] Kafka 이벤트 전달 정상
- [ ] 데이터 매핑 (manuscriptId, authorId 등) 정상
- [ ] 전체 플로우 End-to-End 테스트 성공

---

## 🎯 **추천 사용법**

### **개발 중:**
- `api-test.http` 파일로 개별 API 테스트

### **통합 테스트:**
- `./test-api.sh` 스크립트로 전체 플로우 자동 테스트

### **데모/발표:**
- Postman 컬렉션으로 시각적으로 시연

### **CI/CD:**
- cURL 기반 스크립트로 자동화된 테스트

---

## 💡 **고급 팁**

### **성능 테스트:**
```bash
# 동시 요청 테스트 (Apache Bench)
ab -n 100 -c 10 http://localhost:8086/actuator/health

# 부하 테스트 (wrk)
wrk -t12 -c400 -d30s http://localhost:8086/manuscripts
```

### **환경별 설정:**
```bash
# 개발 환경
export BASE_URL="http://localhost:8086"

# 스테이징 환경  
export BASE_URL="http://staging.example.com"

# 프로덕션 환경
export BASE_URL="http://api.example.com"
```

이제 완벽한 HTTP 테스트 환경이 준비되었습니다! 🚀

**가장 추천하는 방법**: `./test-api.sh` 스크립트로 전체 테스트 후, 개별 테스트는 `api-test.http` 파일 사용!
