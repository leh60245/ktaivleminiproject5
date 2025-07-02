package ktaivleminitocode.domain;

import java.util.Date;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

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
        this.manuscriptId = aggregate.getManuscriptId();
        this.authorId     = aggregate.getAuthorId();
        this.title        = aggregate.getTitle();
        this.content      = aggregate.getContent();
        this.status       = aggregate.getStatus();
        this.createdDate  = aggregate.getCreatedDate();
    }

    public PublishingRequested() { super(); }
}
