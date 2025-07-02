package ktaivleminitocode.domain;

import java.util.*;
import ktaivleminitocode.domain.*;
import ktaivleminitocode.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class PublicationProcessingStarted extends AbstractEvent {

    private Long publicationRequestId;
    private Object status;
    private Long authorid;
    private String content;
    private String category;
    private String summary;
    private String coverImageUrl;
}
