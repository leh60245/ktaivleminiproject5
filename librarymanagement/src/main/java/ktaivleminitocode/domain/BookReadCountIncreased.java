package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookReadCountIncreased extends AbstractEvent {

    private Long bookId;
    private Integer readCount;

    public BookReadCountIncreased(Book aggregate) {
        super(aggregate);
    }

    public BookReadCountIncreased() {
        super();
    }
}
//>>> DDD / Domain Event
