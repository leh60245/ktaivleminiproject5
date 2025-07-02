package ktaivleminitocode.domain;

import java.util.Date;
import lombok.Data;

/**
 * ManuscriptPublication 서비스에서 발행되는 이벤트를
 * librarymanagement 측에서 그대로 역직렬화-수신하기 위한 DTO
 */
@Data
public class PublishingRequested {

    private Long manuscriptId;
    private Long authorId;
    private String title;
    private String content;
    private String status;      // "PUBLISHING_REQUESTED" 등
    private Date createdDate;

    /** 간단 유효성 검증 (선택) */
    public boolean validate() {
        return title != null && authorId != null;
    }
}
