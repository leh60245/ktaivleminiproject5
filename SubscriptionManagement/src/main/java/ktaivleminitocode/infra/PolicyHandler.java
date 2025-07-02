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
    SubscriptionRepository subscriptionRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReadBookPlaced'"
    )
    public void wheneverReadBookPlaced_CheckSubscription(
        @Payload ReadBookPlaced readBookPlaced
    ) {
        ReadBookPlaced event = readBookPlaced;
        System.out.println(
            "\n\n##### listener CheckSubscription : " + readBookPlaced + "\n\n"
        );

        // Sample Logic //
        Subscription.checkSubscription(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
