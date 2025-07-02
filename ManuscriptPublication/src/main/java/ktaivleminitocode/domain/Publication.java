package ktaivleminitocode.domain;

import java.util.Date;
import javax.persistence.*;
import ktaivleminitocode.ManuscriptPublicationApplication;
import lombok.Data;

@Entity
@Table(name = "PublicationRequest_table")
@Data
//<<< DDD / Aggregate Root
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long publicationRequestId;

    private PublicationFormat format;

    private PublicationStatus status;

    private Date publishedDate;

    private Date createdDate;

    @Column(length = 100) // 짧은 문자열
    private String category;

    @Lob // 긴 텍스트 (자동으로 CLOB 또는 TEXT 타입으로 매핑됨)
    @Column
    private String summary;

    @Column(length = 1000) // URL이긴 하지만 GPT가 base64 URL 등을 반환할 가능성도 있음
    private String coverImageUrl;

    private Long authorId;

    private String title;

    private String content;

    public static PublicationRepository repository() {
        PublicationRepository publicationRepository = ManuscriptPublicationApplication.applicationContext.getBean(
            PublicationRepository.class
        );
        return publicationRepository;
    }

    public static Publication handlePublicationRequested(PublicationRequested event) {
        Publication publication = new Publication();

        publication.setCreatedDate(new Date());
        publication.setStatus(PublicationStatus.PROCESSING);

        // 핵심 정보 설정
        publication.setAuthorId(event.getAuthorId());
        publication.setTitle(event.getTitle());
        publication.setContent(event.getContent());

        publication.setCategory(null);          // GPT가 생성
        publication.setSummary(null);           // GPT가 생성
        publication.setCoverImageUrl(null);     // GPT가 생성

        // DB 저장
        repository().save(publication);

        // 이벤트 발행을 위한 값 세팅 (여기 중요!!)
        GptContentGenerationStarted processingStarted = new GptContentGenerationStarted(publication);
        processingStarted.setPublicationRequestId(publication.getPublicationRequestId());
        processingStarted.setAuthorId(publication.getAuthorId());
        processingStarted.setTitle(publication.getTitle());
        processingStarted.setContent(publication.getContent());
        processingStarted.setStatus(publication.getStatus());

        processingStarted.publishAfterCommit();

        return publication;
    }



}
//>>> DDD / Aggregate Root
