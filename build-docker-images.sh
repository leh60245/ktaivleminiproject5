#!/bin/bash

# Docker 이미지 빌드 및 푸시 스크립트

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🐳 Docker 이미지 빌드 및 푸시${NC}"
echo "=========================================="

# 설정
ACR_NAME="ktaivleminiproject5.azurecr.io"
VERSION=${1:-latest}

# 서비스 목록 (디렉토리명:이미지명)
declare -A services=(
    ["AuthorManagement"]="author-management"
    ["SubscriptionManagement"]="subscription-management"
    ["librarymanagement"]="library-management"
    ["pointmanagement"]="point-management"
    ["ManuscriptPublication"]="manuscript-publication"
    ["gateway"]="gateway"
)

echo -e "${YELLOW}📦 빌드할 버전: $VERSION${NC}"
echo -e "${YELLOW}🏗️ ACR: $ACR_NAME${NC}"
echo ""

# 1. Maven 빌드 및 Docker 이미지 생성
for service_dir in "${!services[@]}"; do
    image_name="${services[$service_dir]}"

    echo -e "${BLUE}🔨 $service_dir 빌드 중...${NC}"

    # 서비스 디렉토리로 이동
    cd "$service_dir"

    # Maven 빌드
    echo -e "${YELLOW}  📋 Maven 빌드 중...${NC}"
    mvn clean package -DskipTests -q

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}  ✅ Maven 빌드 성공${NC}"
    else
        echo -e "${RED}  ❌ Maven 빌드 실패${NC}"
        cd ..
        continue
    fi

    # Docker 이미지 빌드
    echo -e "${YELLOW}  🐳 Docker 이미지 빌드 중...${NC}"
    docker build -t "$image_name:$VERSION" -t "$ACR_NAME/$image_name:$VERSION" .

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}  ✅ Docker 이미지 빌드 성공: $image_name:$VERSION${NC}"
    else
        echo -e "${RED}  ❌ Docker 이미지 빌드 실패${NC}"
        cd ..
        continue
    fi

    cd ..
    echo ""
done

# 2. Docker 이미지 목록 확인
echo -e "${YELLOW}📋 생성된 Docker 이미지 목록:${NC}"
for service_dir in "${!services[@]}"; do
    image_name="${services[$service_dir]}"
    if docker images | grep -q "$image_name"; then
        size=$(docker images --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}" | grep "$image_name" | grep "$VERSION" | awk '{print $3}')
        echo -e "${GREEN}✅ $image_name:$VERSION ($size)${NC}"
    else
        echo -e "${RED}❌ $image_name:$VERSION${NC}"
    fi
done

# 3. ACR 푸시 옵션
echo ""
echo -e "${BLUE}🚀 Azure Container Registry에 푸시하시겠습니까? (y/n)${NC}"
read -r push_choice

if [[ $push_choice == "y" || $push_choice == "Y" ]]; then
    echo -e "${YELLOW}🔐 ACR 로그인 확인 중...${NC}"

    # ACR 로그인 (Azure CLI 필요)
    if command -v az &> /dev/null; then
        az acr login --name ktaivleminiproject5 2>/dev/null || {
            echo -e "${RED}❌ ACR 로그인 실패. Azure CLI로 먼저 로그인하세요.${NC}"
            echo -e "${BLUE}💡 명령어: az login && az acr login --name ktaivleminiproject5${NC}"
            exit 1
        }
        echo -e "${GREEN}✅ ACR 로그인 성공${NC}"
    else
        echo -e "${RED}❌ Azure CLI가 설치되지 않음${NC}"
        echo -e "${BLUE}💡 Azure CLI 설치 후 다시 시도하세요${NC}"
        exit 1
    fi

    # 이미지 푸시
    for service_dir in "${!services[@]}"; do
        image_name="${services[$service_dir]}"
        echo -e "${BLUE}📤 $image_name 푸시 중...${NC}"

        docker push "$ACR_NAME/$image_name:$VERSION"

        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✅ $image_name 푸시 성공${NC}"
        else
            echo -e "${RED}❌ $image_name 푸시 실패${NC}"
        fi
    done
fi

# 4. 로컬 Docker Compose 테스트 옵션
echo ""
echo -e "${BLUE}🧪 로컬에서 Docker Compose로 테스트하시겠습니까? (y/n)${NC}"
read -r test_choice

if [[ $test_choice == "y" || $test_choice == "Y" ]]; then
    echo -e "${YELLOW}🚀 Docker Compose 시작 중...${NC}"
    docker-compose up -d

    echo -e "${YELLOW}⏳ 서비스 시작 대기 중 (30초)...${NC}"
    sleep 30

    echo -e "${BLUE}🔍 서비스 상태 확인:${NC}"
    docker-compose ps

    echo -e "\n${BLUE}💡 서비스 테스트:${NC}"
    echo "- Gateway: http://localhost:8088"
    echo "- 헬스체크: ./test-kafka-communication.sh"
    echo "- 로그 확인: docker-compose logs -f [서비스명]"
    echo "- 종료: docker-compose down"
fi

echo -e "\n${GREEN}🎉 Docker 이미지 빌드 완료!${NC}"
echo "=========================================="
echo -e "${BLUE}💡 생성된 파일들:${NC}"
echo "- Docker 이미지들: $(echo ${services[@]} | tr ' ' '\n' | sed 's/^/  /')"
echo "- 배포 스크립트: ./deploy-to-azure.sh"
echo "- 통신 테스트: ./test-kafka-communication.sh"
