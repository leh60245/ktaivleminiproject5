package ktaivleminitocode.domain;

import java.util.Date;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegisterBookRequested extends AbstractEvent {

    private Long publicationRequestId;
    private Long manuscriptId; // 원고 ID 추가
    private Long authorId;
    private String title;
    private String content;
    private String category;
    private String summary;
    private String coverImageUrl;

    public RegisterBookRequested(Publication publication) {
        super(publication);
        this.publicationRequestId = publication.getPublicationRequestId();
        this.manuscriptId = publication.getManuscriptId(); // 원고 ID 초기화
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
