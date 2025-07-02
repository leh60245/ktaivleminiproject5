package ktaivleminitocode.domain;

import java.util.Date;
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
        this.manuscriptId = aggregate.getManuscriptId();
        this.authorId = aggregate.getAuthorId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.status = aggregate.getStatus();
        this.createdDate = aggregate.getCreatedDate();
    }

    public ManuscriptPlaced() {
        super();
    }
}
//>>> DDD / Domain Event
