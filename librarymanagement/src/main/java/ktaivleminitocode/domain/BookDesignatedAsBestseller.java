package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookDesignatedAsBestseller extends AbstractEvent {

    private Long bookId;
    private Boolean bestsellerBadge;
    // 필요한 경우 지정된 시간 등 추가 정보 포함 가능
    // private Date designatedAt;

    public BookDesignatedAsBestseller(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.bestsellerBadge = aggregate.getBestsellerBadge(); // true로 설정될 것
        // this.designatedAt = new Date(); // 이벤트 발생 시점 기록
    }

    public BookDesignatedAsBestseller() {
        super();
    }
}
//>>> DDD / Domain Event
