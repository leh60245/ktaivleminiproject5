package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorRegistrationApproved extends AbstractEvent {

    private Long authorId;
    private AuthorStatus status;
    private Date approvedAt;
    private Date updatedAt; // 업데이트 시간도 이벤트에 포함

    public AuthorRegistrationApproved(Author aggregate) {
        super(aggregate);
        // 애그리게이트의 현재 상태를 이벤트에 매핑
        this.authorId = aggregate.getAuthorId();
        this.status = aggregate.getStatus(); // APPROVED 상태
        this.approvedAt = aggregate.getApprovedAt();
        this.updatedAt = aggregate.getUpdatedAt();
    }

    public AuthorRegistrationApproved() {
        super();
    }
}
//>>> DDD / Domain Event
