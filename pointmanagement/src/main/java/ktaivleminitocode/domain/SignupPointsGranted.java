package ktaivleminitocode.domain;

import java.util.Date;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class SignupPointsGranted extends AbstractEvent {

    private Long userId;
    private Integer updatePoints;
    private Date createdAt;

    public SignupPointsGranted(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getId();
        this.updatePoints = 1000; // 일반 회원가입 포인트 정책
        this.createdAt = new Date();
    }
} 
