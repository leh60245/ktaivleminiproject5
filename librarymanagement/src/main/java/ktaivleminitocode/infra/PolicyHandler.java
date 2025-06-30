package ktaivleminitocode.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import ktaivleminitocode.config.kafka.KafkaProcessor;
import ktaivleminitocode.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    BookRepository bookRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PublicationProcessingStarted'"
    )
    public void wheneverPublicationProcessingStarted_RegisterBook(
        @Payload PublicationProcessingStarted publicationProcessingStarted
    ) {
        PublicationProcessingStarted event = publicationProcessingStarted;
        System.out.println(
            "\n\n##### listener RegisterBook : " +
            publicationProcessingStarted +
            "\n\n"
        );

        // Sample Logic //
        Book.registerBook(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointDeducted'"
    )
    public void wheneverPointDeducted_ReadBook(
        @Payload PointDeducted pointDeducted
    ) {
        PointDeducted event = pointDeducted;
        System.out.println(
            "\n\n##### listener ReadBook : " + pointDeducted + "\n\n"
        );

        // Sample Logic //
        Book.readBook(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionChecked'"
    )
    public void wheneverSubscriptionChecked_ReadBook(
        @Payload SubscriptionChecked subscriptionChecked
    ) {
        SubscriptionChecked event = subscriptionChecked;
        System.out.println(
            "\n\n##### listener ReadBook : " + subscriptionChecked + "\n\n"
        );

        // Sample Logic //
        Book.readBook(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
