package ktaivleminitocode.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import ktaivleminitocode.config.kafka.KafkaProcessor;
import ktaivleminitocode.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
//팔요한 폴리시 핸들링연결
//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    UserRepository userRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointExhausted'"
    )
    public void wheneverPointExhausted_RecommendPlanOnPointExhausted(
        @Payload PointExhausted pointExhausted
    ) {
        PointExhausted event = pointExhausted;
        System.out.println(
            "\n\n##### listener RecommendPlanOnPointExhausted : " +
            pointExhausted +
            "\n\n"
        );
        // Comments //
        //구독자의 포인트가 모두 소진되면 도서 열람이 불가하므로, KT 이동 및 특정 요금제 가입을 추천하는 알림을 제공함

        // Sample Logic //

    }

   @StreamListener(
    value = KafkaProcessor.INPUT,
    condition = "headers['type']=='SubscriptionChecked'"
)
public void wheneverSubscriptionChecked_CheckPoint(
    @Payload SubscriptionChecked subscriptionChecked
) {
    SubscriptionChecked event = subscriptionChecked;
    System.out.println(
        "\n\n##### listener CheckPoint : " + subscriptionChecked + "\n\n"
    );

    // ✅ 사용자 ID로 User 인스턴스를 조회하고, checkPoint 메서드 호출
    userRepository.findById(event.getUserId()).ifPresent(user -> {
        user.checkPoint(event);
    });
}
}
//>>> Clean Arch / Inbound Adaptor
