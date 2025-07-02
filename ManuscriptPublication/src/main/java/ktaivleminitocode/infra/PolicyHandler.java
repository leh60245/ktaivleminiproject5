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

        publicationRepository.findById(event.getPublicationRequestId()).ifPresent(req -> {
            Map<String, String> result = gptService.generateCategoryAndSummary(req.getTitle(), req.getContent());
            String category = result.get("category");
            String summary = result.get("summary");
            String coverImageUrl = gptService.generateCoverImage(req.getTitle(), category);

            req.setCategory(category);
            req.setSummary(summary);
            req.setCoverImageUrl(coverImageUrl);

            if (summary.equals("[요약 실패]") || coverImageUrl.contains("fail.jpg")) {
                req.setStatus(PublicationStatus.FAILED);
            } else {
                req.setStatus(PublicationStatus.COMPLETED);
                req.setPublishedDate(new java.util.Date()); // 출판 완료 시점 기록
            }

            publicationRepository.save(req);

            logger.info("🎉 GPT 처리 완료 → category/summary/coverImageUrl 저장 완료");

            RegisterBookRequested eventToSend = new RegisterBookRequested(req);
            eventToSend.publishAfterCommit();

            logger.info("📘 도서 등록 요청 이벤트 발행 완료 → RegisterBookRequested");
        });
    }
}
