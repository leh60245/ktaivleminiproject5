#!/bin/bash

# Azure 배포 스크립트
# 이 스크립트는 Azure Container Registry와 AKS를 사용하여 마이크로서비스를 배포합니다.

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 설정 변수
RESOURCE_GROUP="rg-ktaivleminiproject5"
AKS_CLUSTER="aks-ktaivleminiproject5"
ACR_NAME="ktaivleminiproject5"
LOCATION="koreacentral"

echo -e "${BLUE}🚀 Azure 배포 스크립트 시작${NC}"
echo "=========================================="

# 1. Azure 리소스 그룹 생성
echo -e "${YELLOW}📦 리소스 그룹 생성 중...${NC}"
if az group show --name $RESOURCE_GROUP >/dev/null 2>&1; then
    echo -e "${GREEN}✅ 리소스 그룹이 이미 존재합니다: $RESOURCE_GROUP${NC}"
else
    az group create --name $RESOURCE_GROUP --location $LOCATION
    echo -e "${GREEN}✅ 리소스 그룹 생성 완료: $RESOURCE_GROUP${NC}"
fi

# 2. Azure Container Registry 생성
echo -e "${YELLOW}🏗️ Azure Container Registry 생성 중...${NC}"
if az acr show --name $ACR_NAME --resource-group $RESOURCE_GROUP >/dev/null 2>&1; then
    echo -e "${GREEN}✅ ACR이 이미 존재합니다: $ACR_NAME${NC}"
else
    az acr create --resource-group $RESOURCE_GROUP --name $ACR_NAME --sku Basic
    echo -e "${GREEN}✅ ACR 생성 완료: $ACR_NAME${NC}"
fi

# 3. ACR 로그인
echo -e "${YELLOW}🔐 ACR 로그인 중...${NC}"
az acr login --name $ACR_NAME
echo -e "${GREEN}✅ ACR 로그인 완료${NC}"

# 4. AKS 클러스터 생성
echo -e "${YELLOW}☸️ AKS 클러스터 생성 중...${NC}"
if az aks show --name $AKS_CLUSTER --resource-group $RESOURCE_GROUP >/dev/null 2>&1; then
    echo -e "${GREEN}✅ AKS 클러스터가 이미 존재합니다: $AKS_CLUSTER${NC}"
else
    az aks create \
        --resource-group $RESOURCE_GROUP \
        --name $AKS_CLUSTER \
        --node-count 2 \
        --node-vm-size Standard_B2s \
        --attach-acr $ACR_NAME \
        --enable-managed-identity \
        --generate-ssh-keys
    echo -e "${GREEN}✅ AKS 클러스터 생성 완료${NC}"
fi

# 5. AKS 자격 증명 가져오기
echo -e "${YELLOW}🔑 AKS 자격 증명 가져오는 중...${NC}"
az aks get-credentials --resource-group $RESOURCE_GROUP --name $AKS_CLUSTER --overwrite-existing
echo -e "${GREEN}✅ AKS 자격 증명 설정 완료${NC}"

# 6. 애플리케이션 빌드 및 푸시
echo -e "${YELLOW}🔨 애플리케이션 빌드 및 푸시 중...${NC}"

services=("AuthorManagement" "SubscriptionManagement" "librarymanagement" "pointmanagement" "ManuscriptPublication" "gateway")

for service in "${services[@]}"; do
    echo -e "${BLUE}빌드 중: $service${NC}"

    # Maven 빌드
    cd $service
    mvn clean package -DskipTests

    # Docker 이미지 빌드 및 푸시
    service_lower=$(echo $service | tr '[:upper:]' '[:lower:]' | sed 's/management/-management/g')
    docker build -t $ACR_NAME.azurecr.io/$service_lower:latest .
    docker push $ACR_NAME.azurecr.io/$service_lower:latest

    echo -e "${GREEN}✅ $service 빌드 및 푸시 완료${NC}"
    cd ..
done

# 7. Kubernetes 배포
echo -e "${YELLOW}☸️ Kubernetes 배포 중...${NC}"
kubectl apply -f kubernetes/complete-deployment.yaml

echo -e "${YELLOW}⏳ 서비스 시작 대기 중...${NC}"
kubectl wait --for=condition=ready pod -l app=kafka -n ktaivleminiproject5 --timeout=300s
kubectl wait --for=condition=ready pod -l app=gateway -n ktaivleminiproject5 --timeout=300s

# 8. 서비스 상태 확인
echo -e "${YELLOW}🔍 서비스 상태 확인 중...${NC}"
kubectl get pods -n ktaivleminiproject5
kubectl get services -n ktaivleminiproject5

# 9. 외부 IP 확인
echo -e "${YELLOW}🌐 외부 IP 확인 중...${NC}"
echo "Gateway 서비스의 외부 IP를 확인하세요:"
kubectl get service gateway -n ktaivleminiproject5

echo -e "${GREEN}🎉 Azure 배포 완료!${NC}"
echo "=========================================="
echo -e "${BLUE}💡 다음 명령어로 서비스 상태를 모니터링할 수 있습니다:${NC}"
echo "kubectl get pods -n ktaivleminiproject5 -w"
echo ""
echo -e "${BLUE}💡 Gateway 외부 IP 확인:${NC}"
echo "kubectl get service gateway -n ktaivleminiproject5"
