package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookReadCountIncreased extends AbstractEvent {

    private Long bookId;
    private Integer readCount;
    // 필요한 경우 조회 발생 시간, 사용자 ID 등 추가 정보 포함 가능
    // private Date increasedAt;
    // private Long readerUserId;

    public BookReadCountIncreased(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.readCount = aggregate.getReadCount(); // 증가된 조회수
        // this.increasedAt = new Date();
    }

    public BookReadCountIncreased() {
        super();
    }
}
//>>> DDD / Domain Event
