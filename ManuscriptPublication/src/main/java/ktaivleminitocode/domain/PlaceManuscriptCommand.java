package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class PlaceManuscriptCommand {

    private Long authorId;
    private String title;
    private String content;
}
