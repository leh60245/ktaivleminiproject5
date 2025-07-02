package ktaivleminitocode.domain;

import java.util.Date;
import javax.persistence.*;
import ktaivleminitocode.ManuscriptPublicationApplication;
import lombok.Data;

@Entity
@Table(name = "Manuscript_table")
@Data
public class Manuscript {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long manuscriptId;

    private String title;
    private String content;
    private ManuscriptStatus status;
    private Date createdDate;
    private Long authorId;

    /* ------------- 공통 ----------- */
    public static ManuscriptRepository repository() {
        return ManuscriptPublicationApplication.applicationContext.getBean(
            ManuscriptRepository.class);
    }

    /* ------------- 원고 등록 ----------- */
    public void placeManuscript(PlaceManuscriptCommand c) {
        this.authorId    = c.getAuthorId();
        this.title       = c.getTitle();
        this.content     = c.getContent();
        this.status      = ManuscriptStatus.PLACED;
        this.createdDate = new Date();

        ManuscriptPlaced e = new ManuscriptPlaced(this);
        e.publishAfterCommit();
    }

    /* ------------- 출간 요청 ----------- */
    public void publishingRequest() {

        // ① 상태 검증
        if (this.status != ManuscriptStatus.PLACED) {
            throw new IllegalStateException(
                "PLACED 상태의 원고만 출간 요청할 수 있습니다.");
        }

        // ② 상태 변경
        this.status = ManuscriptStatus.PUBLISHING_REQUESTED;

        // ③ 이벤트 발행
        PublishingRequested e = new PublishingRequested(this);
        e.publishAfterCommit();
    }
}
