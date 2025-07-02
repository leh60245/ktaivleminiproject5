# 도서 출간 MSA 시스템

## 📚 프로젝트 개요

이 프로젝트는 도서 출간 과정을 마이크로서비스 아키텍처(MSA)로 구현한 예제입니다. 원고 등록, 출간 요청, GPT 기반 요약/카테고리/표지 생성, 도서 등록 등 출간 전 과정을 서비스별로 분리하여 확장성과 유지보수성을 높였습니다.

---

## 🏗️ 전체 아키텍처

- **ManuscriptPublication**: 원고 등록, 출간 요청, GPT 연동, 출간 상태 관리
- **AuthorManagement**: 저자 정보 및 관리
- **LibraryManagement**: 도서관/도서 정보 관리
- **PointManagement**: 포인트 적립/차감 관리
- **SubscriptionManagement**: 구독 서비스 관리
- **Gateway**: API Gateway 역할, 외부 요청 라우팅
- **Frontend**: Vue 기반 프론트엔드
- **Kafka**: 서비스 간 이벤트 브로커

---

## 🚀 주요 흐름 (Event Driven)

1. **원고 등록**: 사용자가 원고를 등록합니다. (`POST /manuscripts`)
2. **원고 배치/출간 요청**: 등록된 원고의 상태를 변경하거나 출간을 요청합니다. (`POST /manuscripts/{id}/placemanuscript`, `POST /manuscripts/{id}/publication-request`)
3. **이벤트 발행**: 상태 변경 시 Kafka로 이벤트가 발행됩니다.
4. **GPT 처리**: 출간 요청 이벤트를 수신한 서비스가 GPT API를 호출해 요약/카테고리/표지 이미지를 생성합니다.
5. **출간 상태/결과 조회**: 출간 요청의 결과를 조회할 수 있습니다. (`GET /publicationRequests/{id}`)

---

## 🛠️ 서비스별 주요 API

### ManuscriptPublication
- `POST /manuscripts` : 원고 등록
- `POST /manuscripts/{id}/placemanuscript` : 원고 배치(상태만 변경)
- `POST /manuscripts/{id}/publication-request` : 출간 요청
- `GET /publicationRequests/{id}` : 출간 상태/결과 조회

### AuthorManagement, LibraryManagement, PointManagement, SubscriptionManagement
- 각 서비스별로 저자, 도서, 포인트, 구독 관련 API 제공

---

## 🧑‍💻 개발 및 실행 방법

1. **Kafka, Zookeeper 등 인프라 실행**
    - infra/docker-compose.yml 참고
2. **각 서비스 빌드 및 실행**
    - 각 서비스 디렉토리에서 `mvn clean package` 후, `java -jar ...` 또는 도커/쿠버네티스 배포
3. **프론트엔드 실행**
    - frontend 디렉토리에서 `npm install && npm run dev`

---

## 📑 API 명세 (프론트엔드 협업용)

### 1. 원고 등록
- **POST /manuscripts**
- 요청 예시:
```json
{
  "authorId": 1,
  "title": "모두의 스프링 부트",
  "content": "이 책은 스프링 부트 초보자를 위한 책입니다."
}
```
- 응답 예시:
```json
{
  "manuscriptId": 12,
  "title": "모두의 스프링 부트",
  "status": "DRAFT",
  "createdDate": "2025-07-02T12:34:56.000+00:00"
}
```

### 2. 출간 요청
- **POST /manuscripts/{manuscriptId}/publication-request**
- 요청 바디 없음
- 응답 예시:
```json
{
  "publicationRequestId": 5,
  "status": "PROCESSING",
  "title": "모두의 스프링 부트",
  "createdDate": "2025-07-02T12:35:00.000+00:00"
}
```

### 3. 출간 상태 조회
- **GET /publicationRequests/{publicationRequestId}**
- 응답 예시:
```json
{
  "publicationRequestId": 5,
  "status": "COMPLETED",
  "title": "모두의 스프링 부트",
  "content": "이 책은 스프링 부트 초보자를 위한 책입니다.",
  "category": "개발",
  "summary": "스프링 부트 입문자를 위한 실용서입니다.",
  "coverImageUrl": "https://cdn.example.com/covers/abcd.png",
  "publishedDate": "2025-07-02T12:36:00.000+00:00"
}
```
- 상태 값: PROCESSING, COMPLETED, FAILED
- coverImageUrl이 "https://fail.jpg"이면 이미지 생성 실패

---

## 📝 참고 및 협업 가이드

- API 명세와 실제 응답 필드, 상태 값이 일치하는지 항상 확인해 주세요.
- 등록/상태전이/조회 흐름을 반드시 지켜주세요.
- 에러 응답 예시:
```json
{
  "error": "Not Found",
  "message": "해당 ID의 데이터가 존재하지 않습니다."
}
```
- 자세한 문의는 백엔드 담당자에게 연락 바랍니다.

---

## 💡 기타

- Swagger/OpenAPI 문서 제공 시 최신 명세를 참고해 주세요.
- Kafka, GPT API 등 외부 연동은 네트워크 환경에 따라 다를 수 있습니다.
- DB, 인프라, 배포 등은 각 서비스별 README 및 문서를 참고하세요.

---

