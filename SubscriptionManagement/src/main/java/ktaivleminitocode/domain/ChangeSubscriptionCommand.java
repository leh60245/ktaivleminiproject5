package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class ChangeSubscriptionCommand {

    private Long userId;
    private Boolean isSubscription;
}
