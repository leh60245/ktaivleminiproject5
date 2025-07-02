package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionChanged extends AbstractEvent {

    private Long userId;
    private Boolean isSubscription;
    private Date changedAt;

    public SubscriptionChanged(Subscription aggregate) {
        super(aggregate);
    }

    public SubscriptionChanged() {
        super();
    }
}
//>>> DDD / Domain Event
