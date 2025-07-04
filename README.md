# KT AIVLE Mini Project 5 - 마이크로서비스 플랫폼

## 📋 프로젝트 개요
이 프로젝트는 Kafka를 이용한 이벤트 드리븐 마이크로서비스 아키텍처로 구성된 도서 관리 플랫폼입니다.

## 🏗️ 시스템 아키텍처

### 마이크로서비스 구성
- **AuthorManagement** (포트: 8082) - 작가 관리
- **SubscriptionManagement** (포트: 8080) - 구독 관리  
- **librarymanagement** (포트: 8084) - 도서 관리
- **pointmanagement** (포트: 8085) - 포인트 관리
- **ManuscriptPublication** (포트: 8086) - 원고 출간 관리
- **gateway** (포트: 8088) - API 게이트웨이

### 인프라
- **Kafka** (포트: 9092) - 서비스 간 비동기 통신
- **Docker** - 컨테이너화
- **Kubernetes** - 오케스트레이션
- **Azure AKS** - 클라우드 배포

## 🚀 빠른 시작 가이드

### 1단계: Kafka 통신 확인
```bash
# Kafka 실행
cd infra
docker-compose up -d

# 서비스 헬스체크
./test-services-health.sh

# Kafka 통신 테스트
./test-kafka-communication.sh
```

### 2단계: Docker 이미지 빌드
```bash
# 모든 서비스 빌드 및 이미지 생성
./build-docker-images.sh

# 로컬 Docker Compose 테스트
docker-compose up -d
```

### 3단계: Azure 배포
```bash
# Azure 리소스 생성 및 배포
./deploy-to-azure.sh

# Kubernetes 상태 확인
kubectl get pods -n ktaivleminiproject5
kubectl get services -n ktaivleminiproject5
```

## 🔧 개발 환경 설정

### 사전 요구사항
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Azure CLI (Azure 배포 시)
- kubectl (Kubernetes 관리 시)

### 로컬 개발 환경
```bash
# 각 서비스 개별 실행
cd AuthorManagement && mvn spring-boot:run
cd SubscriptionManagement && mvn spring-boot:run
cd librarymanagement && mvn spring-boot:run
cd pointmanagement && mvn spring-boot:run
cd ManuscriptPublication && mvn spring-boot:run
cd gateway && mvn spring-boot:run
```

## 🧪 테스트 가이드

### API 테스트
```bash
# 작가 등록
curl -X POST "http://localhost:8088/authors" \
  -H "Content-Type: application/json" \
  -d '{"authorId":"test-001","name":"테스트 작가","portfolio":"포트폴리오"}'

# 도서 등록
curl -X POST "http://localhost:8088/books" \
  -H "Content-Type: application/json" \
  -d '{"bookId":"book-001","authorId":"test-001","title":"테스트 도서","content":"내용"}'

# 구독 등록
curl -X POST "http://localhost:8088/subscriptions" \
  -H "Content-Type: application/json" \
  -d '{"subscriptionId":"sub-001","userId":"user-001","startDate":"2024-01-01"}'
```

### Kafka 이벤트 모니터링
```bash
# 실시간 이벤트 확인
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic ktaivleminitocode \
  --from-beginning
```

## 🐳 Docker 설정

### 개별 서비스 Dockerfile
모든 서비스는 OpenJDK 17 Alpine 이미지를 기반으로 합니다:
```dockerfile
FROM openjdk:17-jdk-alpine
COPY target/*SNAPSHOT.jar app.jar
EXPOSE [각 서비스별 포트]
ENV TZ=Asia/Seoul
ENTRYPOINT ["java","-Xmx400M","-jar","/app.jar","--spring.profiles.active=docker"]
```

### 통합 Docker Compose
```bash
# 전체 시스템 실행
docker-compose up -d

# 로그 확인
docker-compose logs -f [서비스명]

# 시스템 종료
docker-compose down
```

## ☸️ Kubernetes 배포

### Azure AKS 배포
```bash
# 리소스 생성 및 배포
./deploy-to-azure.sh

# 배포 상태 확인
kubectl get all -n ktaivleminiproject5

# 서비스 스케일링
kubectl scale deployment author-management --replicas=3 -n ktaivleminiproject5
```

### 로컬 Kubernetes (선택사항)
```bash
# Minikube 사용 시
kubectl apply -f kubernetes/complete-deployment.yaml
kubectl port-forward service/gateway 8088:8088 -n ktaivleminiproject5
```

## 🔍 모니터링 및 디버깅

### 서비스 상태 확인
```bash
# 헬스체크 대시보드
./test-services-health.sh

# 개별 서비스 헬스체크
curl http://localhost:8088/actuator/health
```

### 로그 및 디버깅
```bash
# Docker 로그
docker-compose logs -f [서비스명]

# Kubernetes 로그
kubectl logs -f deployment/[서비스명] -n ktaivleminiproject5

# Kafka 상태 확인
./test-kafka-communication.sh
```

## 🔧 문제 해결

### 일반적인 문제들

1. **Kafka 연결 실패**
   ```bash
   cd infra && docker-compose restart kafka
   ```

2. **서비스 시작 실패**
   ```bash
   mvn clean package -DskipTests
   docker-compose restart [서비스명]
   ```

3. **포트 충돌**
   - 각 서비스가 고유한 포트를 사용하는지 확인
   - `netstat -an | findstr :[포트번호]` (Windows)

4. **Azure 배포 실패**
   ```bash
   az login
   az acr login --name ktaivleminiproject5
   ```

## 📊 성능 및 스케일링

### 권장 리소스
- **개발환경**: CPU 2core, RAM 8GB
- **운영환경**: CPU 4core, RAM 16GB per node

### 스케일링 전략
```bash
# 수평 스케일링
kubectl scale deployment [서비스명] --replicas=3

# 오토스케일링 설정
kubectl autoscale deployment [서비스명] --cpu-percent=70 --min=1 --max=10
```

## 🔐 보안 설정

### Azure 보안
- ACR에서 관리 ID 사용
- Kubernetes Secrets로 민감 정보 관리
- Network Security Groups로 네트워크 보안

### 환경변수 관리
```bash
# OpenAI API Key 설정 (ManuscriptPublication)
kubectl create secret generic openai-secret \
  --from-literal=api-key=your-api-key \
  -n ktaivleminiproject5
```

## 📚 추가 리소스

### 유용한 스크립트들
- `build-docker-images.sh` - Docker 이미지 빌드
- `deploy-to-azure.sh` - Azure 배포
- `test-kafka-communication.sh` - Kafka 통신 테스트
- `test-services-health.sh` - 서비스 헬스체크

### 모니터링 도구
- Kafka Manager (선택사항)
- Prometheus + Grafana (선택사항)
- Azure Monitor (Azure 배포 시)

## 🤝 기여 가이드

1. 브랜치 생성: `git checkout -b feature/새기능`
2. 변경사항 커밋: `git commit -m '새기능 추가'`
3. 브랜치 푸시: `git push origin feature/새기능`
4. Pull Request 생성

## 📞 지원

문제가 발생하면 다음을 확인하세요:
1. `./test-services-health.sh` 실행
2. 로그 파일 확인
3. Kafka 연결 상태 확인
4. Azure 리소스 상태 확인

---

**🎉 성공적인 배포를 위한 체크리스트:**
- [ ] Kafka 정상 동작 확인
- [ ] 모든 서비스 헬스체크 통과
- [ ] Docker 이미지 빌드 성공
- [ ] Azure 리소스 생성 완료
- [ ] Kubernetes 배포 성공
- [ ] API 테스트 통과
