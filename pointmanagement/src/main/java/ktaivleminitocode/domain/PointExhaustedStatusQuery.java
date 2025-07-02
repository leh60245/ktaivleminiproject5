package ktaivleminitocode.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointExhaustedStatusQuery {

    private Long userId;

    // 응답 필드
    private boolean exhausted;
    private int currentPoint;
}
