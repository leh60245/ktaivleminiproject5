package ktaivleminitocode.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceReadBookCommand {

    private Long userId;
    private Long bookId;

    // 향후 필드 확장이 필요한 경우 대비하여 구조 유지
}
