package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class CancelSubscriptionCommand {

    private Long subscriberId;
}
