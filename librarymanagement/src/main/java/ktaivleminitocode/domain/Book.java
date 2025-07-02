package ktaivleminitocode.domain;

import java.util.Date;
import javax.persistence.*;
import ktaivleminitocode.LibrarymanagementApplication;
import lombok.Data;

@Entity
@Table(name = "Book_table")
@Data
public class Book {

    /* ---------- 기본 필드 ---------- */
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;

    private Long   authorId;
    private String title;
    private String content;

    /* 선택(추가) 필드 */
    private String category;
    private String summary;
    private String coverImageUrl;

    /* 통계·상태 */
    private Integer readCount       = 0;
    private Boolean bestsellerBadge = false;
    private Date    publishedDate;

    /* ---------- 공통 리파지토리 ---------- */
    public static BookRepository repository() {
        return LibrarymanagementApplication.applicationContext.getBean(BookRepository.class);
    }

    /* ---------- 베스트셀러 지정 ---------- */
    public void designateBookAsBestseller(DesignateBookAsBestsellerCommand cmd) {

        if (Boolean.TRUE.equals(bestsellerBadge))
            throw new IllegalStateException("이미 베스트셀러입니다.");

        if (publishedDate == null)
            throw new IllegalStateException("출판된 책만 베스트셀러 지정 가능.");

        this.bestsellerBadge = true;

        BookDesignatedAsBestseller e = new BookDesignatedAsBestseller(this);
        e.publishAfterCommit();
    }

    /* ---------- 외부 이벤트 기반 정적 메서드 ---------- */

    /** ★ PublishingRequested 이벤트 → Book 생성 */
    public static void registerBook(PublishingRequested evt) {

        Book book = new Book();

        book.authorId   = evt.getAuthorId();
        book.title      = evt.getTitle();
        book.content    = evt.getContent();
        book.publishedDate  = evt.getCreatedDate();
        book.readCount      = 0;
        book.bestsellerBadge= false;

        repository().save(book);

        /* (선택) 도서 출간 완료됨 이벤트 */
        BookPublished published = new BookPublished(book);
        published.publishAfterCommit();
    }

    /** 조회수 증가 (Point 차감) */
    public static void readBook(PointDeducted evt) {
        repository().findById(evt.getBookId()).ifPresent(book -> {
            book.readCount += 1;
            repository().save(book);

            BookReadCountIncreased inc = new BookReadCountIncreased(book);
            inc.publishAfterCommit();
        });
    }

    /** 조회수 증가 (구독 확인) */
    public static void readBook(SubscriptionChecked evt) {
        if (!Boolean.TRUE.equals(evt.getIsSubscription())) return;

        repository().findById(evt.getBookId()).ifPresent(book -> {
            book.readCount += 1;
            repository().save(book);

            BookReadCountIncreased inc = new BookReadCountIncreased(book);
            inc.publishAfterCommit();
        });
    }
}
