package ktaivleminitocode.domain;

import java.util.Date;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class ReadBookPlaced extends AbstractEvent {

    private Long userId;
    private Long bookId;
    private Integer pointsUsed;
    private Date placedAt;

    public ReadBookPlaced(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getId();
        this.placedAt = new Date();
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setPointsUsed(Integer pointsUsed) {
        this.pointsUsed = pointsUsed;
    }
}
