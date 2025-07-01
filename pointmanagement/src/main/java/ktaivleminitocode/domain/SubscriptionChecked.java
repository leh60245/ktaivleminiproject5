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
    private Date placedAt;

    public ReadBookPlaced(User aggregate, Long bookId) {
        super(aggregate);
        this.userId = aggregate.getId();
        this.bookId = bookId;
        this.placedAt = new Date();
    }
} 
