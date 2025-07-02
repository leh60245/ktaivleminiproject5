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
        System.out.println("\n\n##### listener CheckSubscription : " + readBookPlaced + "\n\n");

        // 1. 구독 여부 조회
        boolean isSubscribed = subscriptionRepository
            .findByUserId(readBookPlaced.getUserId())
            .isPresent();

        // 2. SubscriptionChecked 이벤트 생성
        SubscriptionChecked subscriptionChecked = new SubscriptionChecked();
        subscriptionChecked.setUserId(readBookPlaced.getUserId());
        subscriptionChecked.setBookId(readBookPlaced.getBookId());
        subscriptionChecked.setIsSubscription(isSubscribed);

        // 3. 이벤트 발행
        subscriptionChecked.publishAfterCommit();
    }

}
//>>> Clean Arch / Inbound Adaptor
