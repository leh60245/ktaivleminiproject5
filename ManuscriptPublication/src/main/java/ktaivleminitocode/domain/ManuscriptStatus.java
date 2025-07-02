package ktaivleminitocode.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;

public enum ManuscriptStatus {
    // DRAFT,
    // SAVED,
    // PUBLICATIONREQUESTED,
    // PUBLISHED,
    // REJECTED,
    PLACED,                 // 원고가 등록됨
    PUBLISHING_REQUESTED,   // 출판 요청됨
    PUBLISHED               // 출판 완료됨 (필요 시 추가)
}
