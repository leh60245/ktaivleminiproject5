package ktaivleminitocode.domain;

import java.util.Date;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegisterBookRequested extends AbstractEvent {

    private Long publicationRequestId;
    private Long authorId;
    private String title;
    private String content;
    private String category;
    private String summary;
    private String coverImageUrl;

    public RegisterBookRequested(Publication publication) {
        super(publication);
        this.publicationRequestId = publication.getPublicationRequestId();
        this.authorId = publication.getAuthorId();
        this.title = publication.getTitle();
        this.content = publication.getContent();
        this.category = publication.getCategory();
        this.summary = publication.getSummary();
        this.coverImageUrl = publication.getCoverImageUrl();
    }

    public RegisterBookRequested() {
        super();
    }
}
