package ktaivleminitocode.domain;

import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class PointDeducted extends AbstractEvent {

    private Long userId;
    private Integer updatePoints;
    private Integer points;
    private Long bookId;
}
