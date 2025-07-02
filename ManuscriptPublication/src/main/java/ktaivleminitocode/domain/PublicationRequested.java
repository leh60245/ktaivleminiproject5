package ktaivleminitocode.domain;

import java.util.*;

import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PublicationRequested extends AbstractEvent {

    private Long manuscriptId;
    private Long authorId;
    private String title;
    private String content;
    private String category;
    private ManuscriptStatus status;
    private Date createdDate;

    public PublicationRequested(Manuscript aggregate) {
        super(aggregate);
        this.manuscriptId = aggregate.getManuscriptId();
        this.authorId = aggregate.getAuthorId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.status = aggregate.getStatus();
        this.createdDate = aggregate.getCreatedDate();
    }

    public PublicationRequested() {
        super();
    }
}
//>>> DDD / Domain Event
