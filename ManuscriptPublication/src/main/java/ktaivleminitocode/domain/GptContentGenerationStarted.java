package ktaivleminitocode.domain;

import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class GptContentGenerationStarted extends AbstractEvent {

    private Long publicationRequestId;
    private PublicationStatus status;
    private Long authorId;
    private String title;
    private String content;
    private String category;
    private String summary;
    private String coverImageUrl;

    public GptContentGenerationStarted(Publication aggregate) {
        super(aggregate);
        this.publicationRequestId = aggregate.getPublicationRequestId();
        this.status = aggregate.getStatus();
        this.authorId = aggregate.getAuthorId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.category = aggregate.getCategory();
        this.summary = aggregate.getSummary();
        this.coverImageUrl = aggregate.getCoverImageUrl();
    }

    public GptContentGenerationStarted() {
        super();
    }
}
//>>> DDD / Domain Event
