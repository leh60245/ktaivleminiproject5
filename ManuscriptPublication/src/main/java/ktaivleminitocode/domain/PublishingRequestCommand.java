package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class PublishingRequestCommand {
    private Long manuscriptId;
}

// @Data
// public class PublishingRequestCommand {

//     private Long authorId;
//     private String title;
//     private String content;
// }
