package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PublicationProcessingStarted extends AbstractEvent {

    private Long publicationRequestId;
    private PublicationRequestStatus status;
    private Long authorid;
    private String content;
    private String category;
    private String summary;
    private String coverImageUrl;

    public PublicationProcessingStarted(PublicationRequest aggregate) {
        super(aggregate);
    }

    public PublicationProcessingStarted() {
        super();
    }
}
//>>> DDD / Domain Event
