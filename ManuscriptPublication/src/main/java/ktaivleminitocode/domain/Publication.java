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

    private Long manuscriptId; // 원고 ID 추가

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
        publication.setManuscriptId(event.getManuscriptId()); // manuscriptId 설정 추가
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

    // GPT 처리 완료 후 상태 업데이트 및 RegisterBookRequested 이벤트 발행
    public void completeGptProcessing(String category, String summary, String coverImageUrl) {
        // 1. GPT에서 생성된 내용으로 업데이트
        this.category = category;
        this.summary = summary;
        this.coverImageUrl = coverImageUrl;

        if (summary.equals("[요약 실패]") || coverImageUrl.contains("fail.jpg")) {
            this.status = PublicationStatus.FAILED;
        } else {
            this.status = PublicationStatus.COMPLETED;
            this.publishedDate = new Date();

            // 성공한 경우에만 RegisterBookRequested 이벤트 발행
            RegisterBookRequested registerBookRequested = new RegisterBookRequested(this);
            registerBookRequested.publishAfterCommit();
        }

        // 2. DB 저장
        repository().save(this);
    }

}
//>>> DDD / Aggregate Root
