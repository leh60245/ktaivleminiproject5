package ktaivleminitocode.domain;

import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class SubscriptionChecked extends AbstractEvent {

    private Long userId;
    private boolean subscriptionActive;
    private int requiredPoints;  // 🔹추가된 필드

    public SubscriptionChecked(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getId();
        this.subscriptionActive = Boolean.TRUE.equals(aggregate.getSubscription());
        this.requiredPoints = 1000; // 🔹필요한 포인트 예시값 (상황에 맞게 변경 가능)
    }

    // getter/setter는 Lombok @Data로 자동 생성됨
}
