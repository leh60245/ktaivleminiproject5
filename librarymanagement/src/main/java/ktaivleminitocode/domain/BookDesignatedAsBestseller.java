package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookDesignatedAsBestseller extends AbstractEvent {

    private Long bookId;
    private Boolean bestsellerBadge;

    public BookDesignatedAsBestseller(Book aggregate) {
        super(aggregate);
    }

    public BookDesignatedAsBestseller() {
        super();
    }
}
//>>> DDD / Domain Event
