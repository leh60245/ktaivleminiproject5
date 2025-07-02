package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorProfileUpdated extends AbstractEvent {

    private Long authorId;
    private String name; // 이름이 변경될 수도 있으므로 추가 고려
    // private String profile;
    private String bio; // AuthorProfile에서 가져올 필드
    private String portfolio;
    private Date updatedAt;

    public AuthorProfileUpdated(Author aggregate) {
        super(aggregate);
        // 애그리게이트의 현재 상태를 이벤트에 매핑합니다.
        this.authorId = aggregate.getAuthorId();
        this.name = aggregate.getName(); // 이름도 함께 변경되었을 수 있으므로 포함
        this.bio = aggregate.getProfile() != null ? aggregate.getProfile().getBio() : null;
        this.portfolio = aggregate.getProfile() != null ? aggregate.getProfile().getPortfolio() : null;
        this.updatedAt = aggregate.getUpdatedAt();
    }

    public AuthorProfileUpdated() {
        super();
    }
}
//>>> DDD / Domain Event
