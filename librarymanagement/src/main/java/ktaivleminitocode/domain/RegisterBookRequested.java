package ktaivleminitocode.domain;

import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class RegisterBookRequested extends AbstractEvent {

    private Long publicationRequestId;
    private Long manuscriptId; // 원고 ID 추가
    private Long authorId;
    private String title;
    private String content;
    private String category;
    private String summary;
    private String coverImageUrl;
}
