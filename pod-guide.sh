#!/bin/bash

# Kubernetes Pod 확인 및 관리 가이드
# 작성일: 2025-07-03
# 목적: Pod 상태 확인, 로그 조회, 디버깅 방법 제공

echo "🚢 Kubernetes Pod 관리 가이드"
echo "================================"

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

echo ""
print_title "1. 기본 Pod 조회 명령어"

print_command "kubectl get pods"
echo "   → 현재 네임스페이스의 모든 Pod 목록 조회"

print_command "kubectl get pods -A"
echo "   → 모든 네임스페이스의 Pod 목록 조회"

print_command "kubectl get pods -o wide"
echo "   → Pod의 상세 정보 (IP, 노드 등) 포함하여 조회"

print_command "kubectl get pods --watch"
echo "   → Pod 상태 실시간 모니터링"

echo ""
print_title "2. Pod 상세 정보 확인"

print_command "kubectl describe pod <pod-name>"
echo "   → Pod의 상세 정보, 이벤트, 상태 확인"

print_command "kubectl get pod <pod-name> -o yaml"
echo "   → Pod의 YAML 설정 전체 출력"

print_command "kubectl get pod <pod-name> -o json"
echo "   → Pod의 JSON 형태 정보 출력"

echo ""
print_title "3. Pod 로그 확인"

print_command "kubectl logs <pod-name>"
echo "   → Pod의 로그 조회"

print_command "kubectl logs <pod-name> -f"
echo "   → Pod 로그 실시간 추적 (tail -f와 유사)"

print_command "kubectl logs <pod-name> --previous"
echo "   → 이전에 실행된 컨테이너의 로그 조회"

print_command "kubectl logs <pod-name> -c <container-name>"
echo "   → 멀티 컨테이너 Pod에서 특정 컨테이너 로그 조회"

echo ""
print_title "4. Pod 상태별 필터링"

print_command "kubectl get pods --field-selector=status.phase=Running"
echo "   → 실행 중인 Pod만 조회"

print_command "kubectl get pods --field-selector=status.phase=Pending"
echo "   → 대기 중인 Pod만 조회"

print_command "kubectl get pods --field-selector=status.phase=Failed"
echo "   → 실패한 Pod만 조회"

echo ""
print_title "5. 라벨 기반 Pod 조회"

print_command "kubectl get pods -l app=manuscriptpublication"
echo "   → 특정 라벨을 가진 Pod 조회"

print_command "kubectl get pods -l version=v1"
echo "   → 버전별 Pod 조회"

print_command "kubectl get pods --show-labels"
echo "   → 모든 Pod의 라벨 정보 표시"

echo ""
print_title "6. Pod 디버깅 및 문제 해결"

print_command "kubectl exec -it <pod-name> -- /bin/bash"
echo "   → Pod 내부로 접속 (bash 쉘)"

print_command "kubectl exec -it <pod-name> -- /bin/sh"
echo "   → Pod 내부로 접속 (sh 쉘 - Alpine Linux 등)"

print_command "kubectl port-forward <pod-name> 8080:8080"
echo "   → Pod의 포트를 로컬로 포워딩"

print_command "kubectl top pods"
echo "   → Pod의 CPU/메모리 사용량 확인"

echo ""
print_title "7. 이 프로젝트의 Pod 확인 방법"

echo "현재 프로젝트의 각 서비스별 Pod 확인:"
echo ""

SERVICES=("authormanagement" "pointmanagement" "subscriptionmanagement" "librarymanagement" "gateway" "manuscriptpublication")

for service in "${SERVICES[@]}"; do
    print_command "kubectl get pods -l app=${service}"
    echo "   → ${service} 서비스 Pod 확인"
done

echo ""
print_title "8. 실제 상태 확인 스크립트"

cat << 'EOF'
# 현재 프로젝트 Pod 상태 일괄 확인
check_project_pods() {
    echo "🔍 프로젝트 Pod 상태 확인 중..."

    # 전체 Pod 상태
    echo "📊 전체 Pod 목록:"
    kubectl get pods -o wide

    echo ""
    echo "📊 서비스별 Pod 상태:"

    for service in authormanagement pointmanagement subscriptionmanagement librarymanagement gateway manuscriptpublication; do
        echo "--- $service ---"
        kubectl get pods -l app=$service 2>/dev/null || echo "Pod 없음"
    done

    echo ""
    echo "📊 문제가 있는 Pod:"
    kubectl get pods --field-selector=status.phase!=Running 2>/dev/null || echo "모든 Pod 정상"
}

# 함수 실행
check_project_pods
EOF

echo ""
print_title "9. 유용한 단축 명령어"

print_command "alias kgp='kubectl get pods'"
echo "   → Pod 조회 단축어 설정"

print_command "alias kgpw='kubectl get pods --watch'"
echo "   → Pod 실시간 모니터링 단축어"

print_command "alias kdp='kubectl describe pod'"
echo "   → Pod 상세 정보 단축어"

print_command "alias klf='kubectl logs -f'"
echo "   → 로그 실시간 추적 단축어"

echo ""
print_title "10. Pod 상태 의미"

echo "Pod Status 설명:"
echo "• Pending: 스케줄링 대기 중"
echo "• Running: 정상 실행 중"
echo "• Succeeded: 성공적으로 완료"
echo "• Failed: 실행 실패"
echo "• Unknown: 상태 불명"

echo ""
echo "Pod Condition 설명:"
echo "• PodScheduled: Pod이 노드에 스케줄됨"
echo "• Ready: Pod이 요청을 처리할 준비됨"
echo "• Initialized: 모든 init 컨테이너가 성공적으로 완료"
echo "• ContainersReady: Pod의 모든 컨테이너가 준비됨"

echo ""
print_title "11. 문제 해결 체크리스트"

echo "Pod가 시작되지 않을 때:"
echo "1. kubectl describe pod <pod-name> 으로 이벤트 확인"
echo "2. kubectl logs <pod-name> 으로 로그 확인"
echo "3. 이미지 이름과 태그가 올바른지 확인"
echo "4. 리소스 부족 여부 확인 (kubectl top nodes)"
echo "5. 네트워크 정책이나 보안 정책 확인"

echo ""
print_success "🎉 Kubernetes Pod 관리 가이드 완료!"
print_warning "실제 사용 시에는 네임스페이스를 지정하는 것을 권장합니다: kubectl get pods -n <namespace>"
