package ktaivleminitocode.infra;

import javax.transaction.Transactional;

import ktaivleminitocode.domain.*;
import ktaivleminitocode.external.GptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


@Service
@Transactional
public class PolicyHandler {

    private static final Logger logger = LoggerFactory.getLogger(PolicyHandler.class);

    private final ManuscriptRepository manuscriptRepository;
    private final PublicationRepository publicationRepository;
    private final GptService gptService;

    public PolicyHandler(ManuscriptRepository manuscriptRepository, PublicationRepository publicationRepository, GptService gptService) {
        this.manuscriptRepository = manuscriptRepository;
        this.publicationRepository = publicationRepository;
        this.gptService = gptService;
    }

    @StreamListener(
            value = "event-in",
            condition = "headers['type']=='PublicationRequested'"
    )
    public void handlePublicationRequested(
            @Payload PublicationRequested publicationRequested
    ) {
        logger.info("\n\n✅ [EVENT] PublicationRequested : {}\n", publicationRequested);
        Publication publication = Publication.handlePublicationRequested(publicationRequested);
        publicationRepository.save(publication);
        logger.info("📝 저장된 Publication ID = {}", publication.getPublicationRequestId());
    }

    @StreamListener(
            value = "event-in",
            condition = "headers['type']=='GptContentGenerationStarted'"
    )
    public void handleGptContentGenerationStarted(
            @Payload GptContentGenerationStarted event
    ) {
        logger.info("\n\n🧠 [EVENT] GptContentGenerationStarted : {}\n", event);

        publicationRepository.findById(event.getPublicationRequestId()).ifPresent(publication -> {
            // 1. 텍스트 처리 상태로 변경
            publication.setStatus(PublicationStatus.TEXT_PROCESSING);
            publicationRepository.save(publication);
            logger.info("📝 텍스트 요약 생성 시작...");

            // 2. 텍스트 요약 생성 (동기)
            Map<String, String> result = gptService.generateCategoryAndSummary(publication.getTitle(), publication.getContent());
            String category = result.get("category");
            String summary = result.get("summary");

            // 3. 텍스트 처리 완료, 이미지 처리 상태로 변경
            publication.setCategory(category);
            publication.setSummary(summary);
            publication.setStatus(PublicationStatus.IMAGE_PROCESSING);
            publicationRepository.save(publication);
            logger.info("✅ 텍스트 요약 완료. 🎨 이미지 생성 시작...");

            // 4. 이미지 생성 (비동기) - 완료되면 자동으로 상태 업데이트
            gptService.generateCoverImageAsync(publication.getTitle(), category)
                    .thenAccept(coverImageUrl -> {
                        logger.info("🎉 이미지 생성 완료: " + coverImageUrl);
                        // 새로운 트랜잭션에서 처리
                        try {
                            Publication updatedPublication = publicationRepository.findById(publication.getPublicationRequestId()).orElse(null);
                            if (updatedPublication != null) {
                                updatedPublication.completeGptProcessing(category, summary, coverImageUrl);
                                publicationRepository.save(updatedPublication);
                                logger.info("✅ 출판 요청 처리 완료: ID=" + updatedPublication.getPublicationRequestId());
                            }
                        } catch (Exception e) {
                            logger.error("❌ 출판 상태 업데이트 실패: " + e.getMessage());
                        }
                    })
                    .exceptionally(throwable -> {
                        logger.error("❌ 이미지 생성 실패: " + throwable.getMessage());
                        try {
                            Publication updatedPublication = publicationRepository.findById(publication.getPublicationRequestId()).orElse(null);
                            if (updatedPublication != null) {
                                updatedPublication.completeGptProcessing(category, summary, "https://via.placeholder.com/1024x1024.png?text=Image+Failed");
                                publicationRepository.save(updatedPublication);
                            }
                        } catch (Exception e) {
                            logger.error("❌ 실패 상태 업데이트 실패: " + e.getMessage());
                        }
                        return null;
                    });
        });
    }
}
