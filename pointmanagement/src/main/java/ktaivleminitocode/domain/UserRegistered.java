import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistered {
    private Long userId;

    // 도메인 이벤트의 발행 시각이나 출처 등을 명시하고 싶으면 확장 가능
    // private LocalDateTime occurredAt;
    // private String source;
} 