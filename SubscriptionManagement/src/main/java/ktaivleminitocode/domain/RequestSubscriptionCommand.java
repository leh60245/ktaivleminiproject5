package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class RequestSubscriptionCommand {
    
    private Long userId;
    private Date startDate;
    private Date endDate;

//     private Long subscriberId;
// //    private SubscriptionPlanType planType;
}
