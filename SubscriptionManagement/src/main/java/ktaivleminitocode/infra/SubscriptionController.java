package ktaivleminitocode.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import ktaivleminitocode.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/subscriptions")
@Transactional
public class SubscriptionController {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @RequestMapping(
        value = "/subscriptionschangesubscription",
        method = RequestMethod.PATCH,
        produces = "application/json;charset=UTF-8"
    )
    public Subscription changeSubscription(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody ChangeSubscriptionCommand changeSubscriptionCommand
    ) throws Exception {
        System.out.println(
            "##### /subscription/changeSubscription  called #####"
        );
        Subscription subscription = new Subscription();
        subscription.changeSubscription(changeSubscriptionCommand);
        subscriptionRepository.save(subscription);
        return subscription;
    }

    @RequestMapping(
        value = "/subscriptions/requestsubscription",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Subscription requestSubscription(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RequestSubscriptionCommand requestSubscriptionCommand
    ) throws Exception {
        System.out.println(
            "##### /subscription/requestSubscription  called #####"
        );
        Subscription subscription = new Subscription();
        subscription.requestSubscription(requestSubscriptionCommand);
        subscriptionRepository.save(subscription);
        return subscription;
    }

    @RequestMapping(
        value = "/subscriptionscancelsubscription",
        method = RequestMethod.PATCH,
        produces = "application/json;charset=UTF-8"
    )
    public Subscription cancelSubscription(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody CancelSubscriptionCommand cancelSubscriptionCommand
    ) throws Exception {
        System.out.println(
            "##### /subscription/cancelSubscription  called #####"
        );
        Subscription subscription = new Subscription();
        subscription.cancelSubscription(cancelSubscriptionCommand);
        subscriptionRepository.save(subscription);
        return subscription;
    }
}
//>>> Clean Arch / Inbound Adaptor
