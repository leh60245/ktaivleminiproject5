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
            Map<String, String> result = gptService.generateCategoryAndSummary(publication.getTitle(), publication.getContent());
            String category = result.get("category");
            String summary = result.get("summary");
            String coverImageUrl = gptService.generateCoverImage(publication.getTitle(), category);

            // 도메인 애그리게이트의 메서드 호출로 변경
            publication.completeGptProcessing(category, summary, coverImageUrl);

            logger.info("🎉 GPT 처리 완료 → category/summary/coverImageUrl 저장 및 이벤트 발행 완료");
        });
    }
}
