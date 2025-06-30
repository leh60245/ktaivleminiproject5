package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorRegistrationApproved extends AbstractEvent {

    private Long authorId;
    private AuthorStatus status;
    private Date approvedAt;

    public AuthorRegistrationApproved(Author aggregate) {
        super(aggregate);
    }

    public AuthorRegistrationApproved() {
        super();
    }
}
//>>> DDD / Domain Event
