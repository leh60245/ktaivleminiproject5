package ktaivleminitocode.domain;

import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class SubscriptionChecked extends AbstractEvent {

    private Long userId;
    private boolean subscriptionActive;

    public SubscriptionChecked(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getId();
        this.subscriptionActive = Boolean.TRUE.equals(aggregate.getSubscription());
    }
} 

