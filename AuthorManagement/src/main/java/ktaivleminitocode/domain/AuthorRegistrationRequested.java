package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorRegistrationRequested extends AbstractEvent {

    private Long authorId;
    private String name;
    private String bio; // AuthorProfile에서 가져올 필드
    // private String profile;
    private String portfolio; // AuthorProfile에서 가져올 필드
    private AuthorStatus status;
    private Date createdAt; // 등록 시간 추가

    public AuthorRegistrationRequested(Author aggregate) {
        super(aggregate);
        // 애그리게이트의 현재 상태를 이벤트에 매핑
        this.authorId = aggregate.getAuthorId();
        this.name = aggregate.getName();
        this.bio = aggregate.getProfile() != null ? aggregate.getProfile().getBio() : null;
        this.portfolio = aggregate.getProfile() != null ? aggregate.getProfile().getPortfolio() : null;
        this.status = aggregate.getStatus();
        this.createdAt = aggregate.getCreatedAt();
    }

    public AuthorRegistrationRequested() {
        super();
    }
}
//>>> DDD / Domain Event
