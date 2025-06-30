package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PointDeducted extends AbstractEvent {

    private Long userId;
    private Integer updatePoints;
    private Integer points;
    private Long bookId;

    public PointDeducted(User aggregate) {
        super(aggregate);
    }

    public PointDeducted() {
        super();
    }
}
//>>> DDD / Domain Event
