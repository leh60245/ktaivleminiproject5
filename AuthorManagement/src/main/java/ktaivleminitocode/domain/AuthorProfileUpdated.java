package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorProfileUpdated extends AbstractEvent {

    private Long authorId;
    private String profile;
    private String portfolio;
    private Date updatedAt;

    public AuthorProfileUpdated(Author aggregate) {
        super(aggregate);
    }

    public AuthorProfileUpdated() {
        super();
    }
}
//>>> DDD / Domain Event
