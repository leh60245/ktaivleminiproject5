package ktaivleminitocode.external;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GptService {

    private final RestTemplate restTemplate;

    public GptService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(12000); // 12초
        factory.setReadTimeout(12000);    // 12초
        this.restTemplate = new RestTemplate(factory);
    }

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    /**
     * 카테고리와 요약을 GPT 한 번 호출로 처리
     * @return Map<String, String> (category, summary)
     */
    public Map<String, String> generateCategoryAndSummary(String title, String content) {
        String prompt = "아래 글의 카테고리와 요약을 각각 알려줘.\n" +
                "카테고리는 한 단어로, 요약은 3줄 이내로.\n" +
                "형식: 카테고리: ...\n요약: ...\n" +
                "제목: " + title + "\n내용: " + content;
        String result = callOpenAiTextApi(prompt);
        Map<String, String> map = new HashMap<>();
        // 정규식으로 카테고리와 요약 추출
        Pattern catPattern = Pattern.compile("카테고리: ?(.+)");
        Pattern sumPattern = Pattern.compile("요약: ?([\\s\\S]+)");
        Matcher catMatcher = catPattern.matcher(result);
        Matcher sumMatcher = sumPattern.matcher(result);
        map.put("category", catMatcher.find() ? catMatcher.group(1).trim() : "[분류 실패]");
        map.put("summary", sumMatcher.find() ? sumMatcher.group(1).trim() : "[요약 실패]");
        return map;
    }

    public String generateCoverImage(String title, String category) {
        String prompt = category + " 장르에 어울리는 전자책 표지 이미지를 만들어줘. 세련된 일러스트 스타일로.";
        String imageUrl = callOpenAiImageApi(prompt);
        return imageUrl;
    }

    // 내부 메서드 – ChatGPT 텍스트 생성
    private String callOpenAiTextApi(String prompt) {
        // 테스트 환경에서 더미 키인 경우 더미 응답 반환
        if (openAiApiKey.contains("dummy") || openAiApiKey.contains("test")) {
            return "카테고리: 소설\n요약: 이것은 테스트용 더미 요약입니다. 실제 GPT API를 사용하지 않고 개발 환경에서 동작하기 위한 더미 응답입니다.";
        }

        String url = openAiApiUrl + "/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4.1-nano-2025-04-14");
        body.put("messages", new Object[]{
                Map.of("role", "system", "content", "전자책 출판 플랫폼 요약 시스템입니다."),
                Map.of("role", "user", "content", prompt)
        });
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                var choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return message.get("content").toString().trim();
                }
            }
        } catch (Exception e) {
            System.err.println("GPT API 호출 실패: " + e.getMessage());
            return "[요약 실패]";
        }
        return "[요약 실패]";
    }

    // 내부 메서드 – 이미지 생성 (모델명 명시)
    private String callOpenAiImageApi(String prompt) {
        // 테스트 환경에서 더미 키인 경우 더미 응답 반환
        if (openAiApiKey.contains("dummy") || openAiApiKey.contains("test")) {
            return "https://via.placeholder.com/1024x1024.png?text=Test+Cover+Image";
        }

        String url = openAiApiUrl + "/v1/images/generations";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        Map<String, Object> body = new HashMap<>();
        body.put("model", "dall-e-3"); // 모델명 명시
        body.put("prompt", prompt);
        body.put("n", 1);
        body.put("size", "1024x1024");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                var data = (List<Map<String, Object>>) response.getBody().get("data");
                if (data != null && !data.isEmpty()) {
                    return data.get(0).get("url").toString();
                }
            }
        } catch (Exception e) {
            System.err.println("GPT 이미지 생성 API 호출 실패: " + e.getMessage());
            return "https://fail.jpg";
        }
        return "https://fail.jpg";
    }
}
