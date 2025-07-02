package ktaivleminitocode.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktaivleminitocode.LibrarymanagementApplication;
import ktaivleminitocode.domain.BookPublished;
import ktaivleminitocode.domain.BookReadCountIncreased;
import lombok.Data;

@Entity
@Table(name = "Book_table")
@Data
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;

    private Long authorId;

    private String title;

    private String content;

    private String category;

    private String summary;

    private String coverImageUrl;

    private Integer readCount; // 조회수 (읽은 횟수)

    private Boolean bestsellerBadge; // 베스트셀러 뱃지 여부

    private Date publishedDate; // 출판일

    public static BookRepository repository() {
        BookRepository bookRepository = LibrarymanagementApplication.applicationContext.getBean(
            BookRepository.class
        );
        return bookRepository;
    }

    //<<< Clean Arch / Port Method
    public void designateBookAsBestseller(
        DesignateBookAsBestsellerCommand designateBookAsBestsellerCommand
    ) {
        //implement business logic here:
        // 1. 비즈니스 규칙 검증 (예: 이미 베스트셀러이거나, 출판되지 않은 책은 지정 불가 등)
        if (this.bestsellerBadge != null && this.bestsellerBadge) {
            throw new IllegalStateException("Book is already designated as a bestseller.");
        }
        if (this.publishedDate == null) {
            throw new IllegalStateException("Only published books can be designated as bestsellers.");
        }
        // 커맨드에 bookId만 있고 bestsellerBadge 값은 없으므로,
        // 이 메서드는 해당 책을 '베스트셀러'로 지정하는 역할만 수행한다고 가정합니다.
        // 만약 커맨드에 뱃지 종류를 지정하는 필드가 있다면 그에 따라 로직을 추가합니다.

        // 2. 애그리게이트 상태 변경
        this.bestsellerBadge = true; // 베스트셀러 뱃지 활성화

        // 3. 이벤트 발행
        BookDesignatedAsBestseller bookDesignatedAsBestseller = new BookDesignatedAsBestseller(
            this
        );
        bookDesignatedAsBestseller.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void registerBook( // static 메서드는 외부 이벤트를 받아 Aggregate를 생성하거나 찾아서 처리할 때 사용
        PublicationProcessingStarted publicationProcessingStarted
    ) {
        //implement business logic here:
        // 1. PublicationProcessingStarted 이벤트로부터 새로운 Book 애그리게이트 생성
        Book book = new Book();
        // 이벤트의 데이터를 Book 애그리게이트에 매핑
        book.setAuthorId(publicationProcessingStarted.getAuthorid());
        book.setTitle(publicationProcessingStarted.getContent()); // 다이어그램에 content는 없으나 ManuscriptPublication에서 title로 사용될 가능성
        // publicationProcessingStarted.getContent()가 'content'가 아니라 'title'일 가능성이 높음.
        // 이벤트 스토밍 다이어그램의 '출간 완료됨' 이벤트 (출간 준비됨 이벤트와 유사)를 보면 title이 있음.
        // 여기서는 title로 가정하고, 실제 ManuscriptPublication의 PublicationProcessingStarted 필드명을 확인해야 함.

        book.setCategory(publicationProcessingStarted.getCategory());
        book.setSummary(publicationProcessingStarted.getSummary());
        book.setCoverImageUrl(publicationProcessingStarted.getCoverImageUrl());
        book.setPublishedDate(new Date()); // 현재 시간으로 출판일 기록
        book.setReadCount(0); // 초기 조회수는 0
        book.setBestsellerBadge(false); // 초기에는 베스트셀러 아님

        // 2. 생성된 Book 애그리게이트 저장
        repository().save(book);

        // 3. '도서 출간 완료됨' 이벤트 발행
        BookPublished bookPublished = new BookPublished(book);
        bookPublished.publishAfterCommit();

    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void readBook(PointDeducted pointDeducted) {
        //implement business logic here:
        // 1. PointDeducted 이벤트의 bookId를 사용하여 Book 애그리게이트를 찾음
        repository().findById(pointDeducted.getBookId()).ifPresent(book -> {
            // 2. 비즈니스 로직: 조회수 증가
            book.setReadCount(book.getReadCount() + 1);

            // 3. 변경된 Book 애그리게이트 저장
            repository().save(book);

            // 4. '도서 열람 수 증가됨' 이벤트 발행
            BookReadCountIncreased bookReadCountIncreased = new BookReadCountIncreased(book);
            bookReadCountIncreased.publishAfterCommit();
        });
        // 만약 bookId에 해당하는 책을 찾을 수 없으면 아무것도 하지 않음 (예외 처리도 가능)
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    // '구독 확인됨' 이벤트(외부 바운더리: SubscriptionManagement)를 받아 '도서 열람' 처리
    public static void readBook(SubscriptionChecked subscriptionChecked) {
        // 구독 상태 확인
        if (!Boolean.TRUE.equals(subscriptionChecked.getIsSubscription())) {
            System.out.println(" 구독 안됨 - 열람 차단");
            return; // 구독이 없으면 열람 불가
        }

        repository().findById(subscriptionChecked.getBookId()).ifPresent(book -> {
            // 구독 확인된 사용자에 한해 열람 처리
            book.setReadCount(book.getReadCount() + 1);
            repository().save(book);

            BookReadCountIncreased event = new BookReadCountIncreased(book);
            event.publishAfterCommit();

            System.out.println(" 구독 확인됨 - 열람 기록 처리 완료");
        });
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
