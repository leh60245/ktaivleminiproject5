package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ManuscriptPlaced extends AbstractEvent {

    private Long manuscriptId;
    private Long authorId;
    private String title;
    private String content;
//    private ManuscriptType type;
    private ManuscriptStatus status;
    private Date createdDate;

    public ManuscriptPlaced(Manuscript aggregate) {
        super(aggregate);
    }

    public ManuscriptPlaced() {
        super();
    }
}
//>>> DDD / Domain Event
