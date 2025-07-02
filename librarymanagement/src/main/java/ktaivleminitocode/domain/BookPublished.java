package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookPublished extends AbstractEvent {

    private Long bookId;
    private Long authorId;
    private String title; // 도서 제목 추가 (이벤트 스토밍 다이어그램에 있음)
    private Date publishedDate;

    public BookPublished(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.authorId = aggregate.getAuthorId();
        this.title = aggregate.getTitle(); // Book에서 가져옴
        this.publishedDate = aggregate.getPublishedDate();
    }

    public BookPublished() {
        super();
    }
}
//>>> DDD / Domain Event
