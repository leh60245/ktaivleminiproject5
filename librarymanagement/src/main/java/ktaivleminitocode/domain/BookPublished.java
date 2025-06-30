package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookPublished extends AbstractEvent {

    private Long bookId;
    private Long authorId;
    private Date publishedDate;

    public BookPublished(Book aggregate) {
        super(aggregate);
    }

    public BookPublished() {
        super();
    }
}
//>>> DDD / Domain Event
