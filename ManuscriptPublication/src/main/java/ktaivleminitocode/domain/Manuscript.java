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

    public static ManuscriptRepository repository() {
        ManuscriptRepository manuscriptRepository = ManuscriptPublicationApplication.applicationContext.getBean(
                ManuscriptRepository.class
        );
        return manuscriptRepository;
    }

    // 원고 등록(생성)용 메서드 - command로 값 할당, 상태 설정, 저장, 이벤트 발행
    public void placeManuscript(PlaceManuscriptCommand placeManuscriptCommand) {
        this.title = placeManuscriptCommand.getTitle();
        this.content = placeManuscriptCommand.getContent();
        this.authorId = placeManuscriptCommand.getAuthorId();
        this.createdDate = new Date();
        this.status = ManuscriptStatus.DRAFT;

        // 저장
        repository().save(this);

        // 이벤트 발행
        ManuscriptPlaced event = new ManuscriptPlaced(this);
        event.publishAfterCommit();
    }

    // 원고 배치(상태만 변경)용 메서드 - 값은 건드리지 않고 상태만 변경
    public void placeManuscript() {
        this.status = ManuscriptStatus.DRAFT;

        // 저장
        repository().save(this);

        // 이벤트 발행
        ManuscriptPlaced event = new ManuscriptPlaced(this);
        event.publishAfterCommit();
    }

    public void requestPublication() {
        // 상태 변경
        this.status = ManuscriptStatus.REQUESTED;

        // 저장
        repository().save(this);

        // 이벤트 발행
        PublicationRequested event = new PublicationRequested(this);
        event.publishAfterCommit();
    }
}
