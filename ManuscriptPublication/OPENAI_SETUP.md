# ManuscriptPublication 서비스 - OpenAI API 설정 가이드

## 🚀 빠른 실행 (테스트용)

OpenAI API Key 없이도 테스트할 수 있습니다:

```bash
mvn spring-boot:run
```

서비스가 자동으로 더미 키를 사용하여 테스트 모드로 실행됩니다.

## 🔑 OpenAI API Key 설정 방법

실제 GPT 기능을 사용하려면 OpenAI API Key가 필요합니다. 다음 중 **아무 방법**이나 선택하세요:

### **방법 1: 환경변수 설정 (권장)**

#### Linux/Mac:
```bash
export OPENAI_API_KEY="your-api-key-here"
mvn spring-boot:run
```

#### Windows PowerShell:
```powershell
$env:OPENAI_API_KEY="your-api-key-here"
mvn spring-boot:run
```

#### Windows CMD:
```cmd
set OPENAI_API_KEY=your-api-key-here
mvn spring-boot:run
```

### **방법 2: IDE에서 설정**

**IntelliJ IDEA:**
1. Run Configuration 열기
2. Environment Variables에 추가:
   - `OPENAI_API_KEY` = `your-api-key-here`

**VSCode:**
1. `.vscode/launch.json` 파일에 추가:
```json
{
    "env": {
        "OPENAI_API_KEY": "your-api-key-here"
    }
}
```

### **방법 3: application.yml 직접 수정**

`src/main/resources/application.yml` 파일에서:
```yaml
openai:
  api:
    key: your-api-key-here  # 이 부분만 수정
```

⚠️ **주의**: Git에 API Key가 업로드되지 않도록 주의하세요!

### **방법 4: 실행 시 직접 전달**

```bash
mvn spring-boot:run -Dopenai.api.key="your-api-key-here"
```

## 🔍 API Key 작동 확인

서버 실행 후 로그에서 확인:
- 더미 키 사용 시: "테스트용 더미 응답입니다"
- 실제 키 사용 시: GPT API 실제 호출

## 🚨 문제 해결

### "Could not resolve placeholder 'OPENAI_API_KEY'" 오류
1. 환경변수가 제대로 설정되었는지 확인
2. IDE 재시작 후 다시 실행
3. 방법 3(직접 수정) 사용

### GPT API 호출 실패
1. API Key가 유효한지 확인
2. OpenAI 계정에 크레딧이 있는지 확인
3. 네트워크 연결 상태 확인

## 🎯 테스트 시나리오

1. **개발/테스트**: 더미 키 사용 (자동)
2. **실제 기능 테스트**: 실제 API Key 설정
3. **프로덕션**: 환경변수로 안전하게 설정

더 궁금한 사항은 팀 Slack이나 이슈 트래커에 문의하세요!
