package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionChecked extends AbstractEvent {

    private Long userId;
    private Long bookId;
    private Boolean isSubsription;

    public SubscriptionChecked(Subscription aggregate) {
        super(aggregate);
    }

    public SubscriptionChecked() {
        super();
    }
}
//>>> DDD / Domain Event
