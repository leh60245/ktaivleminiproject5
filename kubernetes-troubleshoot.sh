#!/bin/bash

# Kubernetes 클러스터 상태 진단 및 해결 가이드 (Windows 환경)
# 작성일: 2025-07-03
# 목적: Kubernetes 연결 문제 진단 및 해결

echo "🔧 Kubernetes 클러스터 진단 및 문제 해결 가이드"
echo "=================================================="

# 색상 코드 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

print_title() {
    echo -e "${BLUE}📋 $1${NC}"
    echo "-----------------------------------"
}

print_command() {
    echo -e "${CYAN}💻 $1${NC}"
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

print_info() {
    echo -e "${BLUE}ℹ️ $1${NC}"
}

echo ""
print_title "1. 현재 상황 진단"

print_error "발생한 에러: 'the server could not find the requested resource'"
print_info "이 에러는 다음 중 하나의 문제를 의미합니다:"
echo "   • Kubernetes 클러스터가 실행되지 않음"
echo "   • kubectl이 클러스터에 연결할 수 없음"
echo "   • kubeconfig 설정 문제"
echo "   • API 서버가 응답하지 않음"

echo ""
print_title "2. Windows에서 Kubernetes 설정 확인"

print_command "kubectl cluster-info"
echo "   → 클러스터 정보 확인 (실행 여부 판단)"

print_command "kubectl config get-contexts"
echo "   → 현재 설정된 컨텍스트 확인"

print_command "kubectl config current-context"
echo "   → 현재 사용 중인 컨텍스트 확인"

print_command "kubectl version --client"
echo "   → kubectl 클라이언트 버전 확인"

echo ""
print_title "3. Windows에서 Kubernetes 환경 옵션"

echo "🐳 Option 1: Docker Desktop (권장)"
echo "   • Docker Desktop 설치 후 Kubernetes 활성화"
echo "   • Settings → Kubernetes → Enable Kubernetes 체크"
echo "   • Apply & Restart 클릭"

echo ""
echo "☁️ Option 2: Minikube"
echo "   • 로컬 개발용 단일 노드 클러스터"
echo "   • Windows에서 가장 안정적"

echo ""
echo "🌐 Option 3: 클라우드 클러스터 (AWS EKS, GCP GKE, Azure AKS)"
echo "   • 프로덕션 환경에 적합"
echo "   • kubeconfig 파일 설정 필요"

echo ""
print_title "4. Docker Desktop Kubernetes 설정 방법"

echo "1️⃣ Docker Desktop 설치 확인:"
print_command "docker --version"
echo ""

echo "2️⃣ Docker Desktop에서 Kubernetes 활성화:"
echo "   • Docker Desktop 실행"
echo "   • 설정(Settings) → Kubernetes"
echo "   • 'Enable Kubernetes' 체크박스 선택"
echo "   • 'Show system containers' 체크박스 선택 (선택사항)"
echo "   • 'Apply & Restart' 클릭"
echo ""

echo "3️⃣ 설치 완료 확인:"
print_command "kubectl cluster-info"
echo "   → 정상이면 'Kubernetes control plane is running at...' 메시지 출력"

echo ""
print_title "5. Minikube 설정 방법 (Docker Desktop 대안)"

echo "1️⃣ Minikube 설치:"
echo "   • https://minikube.sigs.k8s.io/docs/start/ 에서 Windows용 다운로드"
echo "   • 또는 Chocolatey: choco install minikube"
echo ""

echo "2️⃣ Minikube 시작:"
print_command "minikube start"
echo "   → 가상머신 생성 후 Kubernetes 클러스터 시작"
echo ""

echo "3️⃣ kubectl 컨텍스트 설정:"
print_command "minikube kubectl -- get po -A"
echo "   → Minikube의 kubectl 사용"
echo ""

echo "4️⃣ 대시보드 실행 (선택사항):"
print_command "minikube dashboard"
echo "   → 웹 기반 Kubernetes 대시보드 실행"

echo ""
print_title "6. 즉시 해결 방법 시도"

echo "💡 다음 명령어들을 순서대로 실행해보세요:"
echo ""

print_command "# 1. 현재 상태 확인"
echo "kubectl version --client"
echo "kubectl config get-contexts"
echo ""

print_command "# 2. Docker Desktop Kubernetes 확인"
echo "docker info | grep -i kubernetes"
echo ""

print_command "# 3. 컨텍스트 확인 및 변경"
echo "kubectl config use-context docker-desktop"
echo ""

print_command "# 4. 클러스터 정보 재확인"
echo "kubectl cluster-info"

echo ""
print_title "7. 문제 해결 체크리스트"

echo "✅ 확인할 사항들:"
echo ""
echo "1. Docker Desktop이 실행 중인가?"
echo "   → 시스템 트레이에서 Docker 아이콘 확인"
echo ""
echo "2. Docker Desktop에서 Kubernetes가 활성화되어 있나?"
echo "   → Settings → Kubernetes → Enable Kubernetes 체크"
echo ""
echo "3. 방화벽이나 프록시가 차단하고 있나?"
echo "   → 회사 네트워크의 경우 IT 부서에 문의"
echo ""
echo "4. kubectl 버전이 호환되나?"
echo "   → kubectl version --client"
echo ""
echo "5. 다른 Kubernetes 컨텍스트가 설정되어 있나?"
echo "   → kubectl config get-contexts"

echo ""
print_title "8. 이 프로젝트를 위한 추천 설정"

echo "현재 MSA 프로젝트를 위한 권장 환경:"
echo ""
print_success "🐳 Docker Desktop + Kubernetes (로컬 개발)"
echo "   • 설치 간단"
echo "   • Windows에서 안정적"
echo "   • 리소스 사용량 적당"
echo ""
print_info "📊 시스템 요구사항:"
echo "   • RAM: 최소 8GB (권장 16GB)"
echo "   • CPU: 2코어 이상"
echo "   • 디스크: 20GB 여유 공간"

echo ""
print_title "9. 대안: Docker Compose 사용"

print_warning "Kubernetes 설정이 복잡하다면 Docker Compose 사용을 고려하세요:"
echo ""
print_command "# 현재 프로젝트에서 Docker Compose 실행"
echo "docker-compose -f infra/docker-compose.yml up -d"
echo "docker-compose -f build-docker-compose.yml up --build"
echo ""
print_info "Docker Compose는 설정이 간단하고 로컬 개발에 충분합니다."

echo ""
print_title "10. 실행할 명령어 요약"

echo "🚀 단계별 실행 명령어:"
echo ""
echo "1️⃣ 현재 상태 확인:"
print_command "kubectl version --client"
print_command "docker --version"
echo ""
echo "2️⃣ Docker Desktop Kubernetes 활성화 후:"
print_command "kubectl cluster-info"
print_command "kubectl get nodes"
echo ""
echo "3️⃣ 프로젝트 배포 테스트:"
print_command "kubectl apply -f kubernetes/"
print_command "kubectl get pods"

echo ""
print_success "🎉 이 가이드가 문제 해결에 도움이 되길 바랍니다!"
print_info "추가 도움이 필요하면 현재 환경 정보를 알려주세요:"
echo "   • 운영체제 정보"
echo "   • Docker Desktop 설치 여부"
echo "   • kubectl version 출력 결과"
