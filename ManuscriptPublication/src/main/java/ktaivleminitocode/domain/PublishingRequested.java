package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PublishingRequested extends AbstractEvent {

    private Long manuscriptId;
    private Long authorId;
    private String title;
    private String content;
    private ManuscriptStatus status;
    private Date createdDate;

    public PublishingRequested(Manuscript aggregate) {
        super(aggregate);
    }

    public PublishingRequested() {
        super();
    }
}
//>>> DDD / Domain Event
