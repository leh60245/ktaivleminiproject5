package ktaivleminitocode.infra;

import javax.transaction.Transactional;
import ktaivleminitocode.config.kafka.KafkaProcessor;
import ktaivleminitocode.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    BookRepository bookRepository;

    /* ① 원고 출간 준비(PublishingRequested) 수신 → Book 생성 */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PublishingRequested'"
    )
    public void wheneverPublishingRequested_CreateBook(
        @Payload PublishingRequested event
    ) {
        if (!event.validate()) return;

        System.out.println(
            "\n##### PublishingRequested received : " + event + " #####\n"
        );

        /* Book 애그리게이트 생성 */
        Book book = new Book();
        book.setAuthorId(event.getAuthorId());
        book.setTitle(event.getTitle());
        book.setContent(event.getContent());
        book.setPublishedDate(event.getCreatedDate());
        book.setReadCount(0);
        book.setBestsellerBadge(false);

        bookRepository.save(book);

        /* (선택) BookPublished 이벤트 발행 */
        BookPublished published = new BookPublished(book);
        published.publishAfterCommit();
    }

    /* 기존 리스너들 그대로 유지 ↓↓↓ */

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PublicationProcessingStarted'"
    )
    public void wheneverPublicationProcessingStarted_RegisterBook(
        @Payload PublicationProcessingStarted event
    ) {
        System.out.println("\n##### RegisterBook : " + event + " #####\n");
        Book.registerBook(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointDeducted'"
    )
    public void wheneverPointDeducted_ReadBook(@Payload PointDeducted event) {
        Book.readBook(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionChecked'"
    )
    public void wheneverSubscriptionChecked_ReadBook(
        @Payload SubscriptionChecked event
    ) {
        Book.readBook(event);
    }
    
    @StreamListener(
        value     = KafkaProcessor.INPUT,
        condition = "headers['type']=='PublishingRequested'"
    )
    public void wheneverPublishingRequested_RegisterBook(
            @Payload PublishingRequested event) {

        if (!event.validate()) return;          // validate() 메서드는 DTO에 간단히 구현

        System.out.println("### PublishingRequested : " + event);

        Book.registerBook(event);
    }
}
