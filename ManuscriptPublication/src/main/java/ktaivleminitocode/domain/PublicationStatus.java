package ktaivleminitocode.domain;

public enum PublicationStatus {
    PENDING,
    PROCESSING,
    TEXT_PROCESSING,      // 텍스트 요약 생성 중
    IMAGE_PROCESSING,     // 이미지 생성 중
    COMPLETED,
    FAILED,
}
