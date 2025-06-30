package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorRegistrationRequested extends AbstractEvent {

    private Long authorId;
    private String name;
    private String profile;
    private String portfolio;
    private AuthorStatus status;

    public AuthorRegistrationRequested(Author aggregate) {
        super(aggregate);
    }

    public AuthorRegistrationRequested() {
        super();
    }
}
//>>> DDD / Domain Event
