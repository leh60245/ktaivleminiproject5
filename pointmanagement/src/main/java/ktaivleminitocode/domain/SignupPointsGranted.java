package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class SignupPointsGranted extends AbstractEvent {

    private Long userId;
    private Integer updatePoints;
    private Date createdAt;

    public SignupPointsGranted(User aggregate) {
        super(aggregate);
    }

    public SignupPointsGranted() {
        super();
    }
}
//>>> DDD / Domain Event
