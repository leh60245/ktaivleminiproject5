package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ReadBookPlaced extends AbstractEvent {

    private Long userId;
    private Long bookId;

    public ReadBookPlaced(User aggregate) {
        super(aggregate);
    }

    public ReadBookPlaced() {
        super();
    }
}
//>>> DDD / Domain Event
