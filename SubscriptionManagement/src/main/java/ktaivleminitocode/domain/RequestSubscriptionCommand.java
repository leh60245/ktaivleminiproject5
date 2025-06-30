package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class RequestSubscriptionCommand {

    private Long subscriberId;
    private SubscriptionPlanType planType;
}
