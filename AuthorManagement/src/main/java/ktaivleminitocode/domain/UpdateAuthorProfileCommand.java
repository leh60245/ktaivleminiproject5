package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class UpdateAuthorProfileCommand {

    private Long authorId;
    // private String profile;
    private String portfolio;
    private String bio; // AuthorProfile의 bio에 매핑될 필드 추가
}
