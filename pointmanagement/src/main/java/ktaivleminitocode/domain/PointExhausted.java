package ktaivleminitocode.domain;

import java.util.Date;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class PointExhausted extends AbstractEvent {

    private Long userId;
    private Date exhaustedAt;

    public PointExhausted(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getId();
        this.exhaustedAt = new Date();
    }
} 
