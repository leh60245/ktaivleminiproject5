package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class KtBonusPointsGranted extends AbstractEvent {

    private Long userId;
    private Integer updatePoints;
    private Date createdAt;

    public KtBonusPointsGranted(User aggregate) {
        super(aggregate);
    }

    public KtBonusPointsGranted() {
        super();
    }
}
//>>> DDD / Domain Event
