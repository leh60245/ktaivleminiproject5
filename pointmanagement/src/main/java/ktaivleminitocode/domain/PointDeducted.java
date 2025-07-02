package ktaivleminitocode.domain;

import java.util.Date;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class PointDeducted extends AbstractEvent {

    private Long userId;
    private Integer deductedAmount;
    private Integer remainingPoints;
    private Date deductedAt;

    public PointDeducted(User aggregate, int deductedAmount) {
        super(aggregate);
        this.userId = aggregate.getId();
        this.deductedAmount = deductedAmount;
        this.remainingPoints = aggregate.getPoint();
        this.deductedAt = new Date();
    }
} 
