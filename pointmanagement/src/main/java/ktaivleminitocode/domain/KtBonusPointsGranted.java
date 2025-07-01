package ktaivleminitocode.domain;

import java.util.Date;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class KtBonusPointsGranted extends AbstractEvent {

    private Long userId;
    private Integer updatePoints;
    private Date createdAt;

    public KtBonusPointsGranted(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getId();
        this.updatePoints = 5000; // KT 고객 보너스는 정책에 따라 고정
        this.createdAt = new Date();
    }
}
