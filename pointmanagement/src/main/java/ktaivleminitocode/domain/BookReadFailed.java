package ktaivleminitocode.domain;

import java.util.Date;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;
// 필요한 빌드 책읽기 실패 이벤트생성
@Data
@ToString
@NoArgsConstructor
public class BookReadFailed extends AbstractEvent {

    private Long userId;
    private Long bookId;
    private Integer requiredPoints;
    private Date failedAt;

    public BookReadFailed(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getId();
        this.failedAt = new Date();
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setRequiredPoints(Integer requiredPoints) {
        this.requiredPoints = requiredPoints;
    }
}