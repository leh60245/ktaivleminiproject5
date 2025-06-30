package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class UpdateAuthorProfileCommand {

    private Long authorId;
    private String profile;
    private String portfolio;
}
