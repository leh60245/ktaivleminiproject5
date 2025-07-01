package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionRequested extends AbstractEvent {

    private Long subscriberId;
//    private SubscriptionPlanType planType;
    private Date requestedAt;

    public SubscriptionRequested(Subscription aggregate) {
        super(aggregate);
    }

    public SubscriptionRequested() {
        super();
    }
}
//>>> DDD / Domain Event
