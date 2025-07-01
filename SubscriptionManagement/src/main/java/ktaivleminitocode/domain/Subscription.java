package ktaivleminitocode.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktaivleminitocode.SubscriptionManagementApplication;
import ktaivleminitocode.domain.SubscriptionChecked;
import lombok.Data;

@Entity
@Table(name = "Subscription_table")
@Data
//<<< DDD / Aggregate Root
public class Subscription {

    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    // private Long subscriptionId;

    // private Long userId;

    // private Date startDate;

    // private Date endDate;

    // private Boolean isSubscription;

    // public static SubscriptionRepository repository() {
    //     SubscriptionRepository subscriptionRepository = SubscriptionManagementApplication.applicationContext.getBean(
    //         SubscriptionRepository.class
    //     );
    //     return subscriptionRepository;
    // }

    // public void ActivateSubscription() {
    //     //
    // }

    // //<<< Clean Arch / Port Method
    // public void changeSubscription(
    //     ChangeSubscriptionCommand changeSubscriptionCommand
    // ) {
    //     //implement business logic here:

    //     SubscriptionChanged subscriptionChanged = new SubscriptionChanged(this);
    //     subscriptionChanged.publishAfterCommit();
    // }

    // //>>> Clean Arch / Port Method
    // //<<< Clean Arch / Port Method
    // public void requestSubscription(
    //     RequestSubscriptionCommand requestSubscriptionCommand
    // ) {
    //     //implement business logic here:

    //     SubscriptionRequested subscriptionRequested = new SubscriptionRequested(
    //         this
    //     );
    //     subscriptionRequested.publishAfterCommit();
    // }

    // //>>> Clean Arch / Port Method
    // //<<< Clean Arch / Port Method
    // public void cancelSubscription(
    //     CancelSubscriptionCommand cancelSubscriptionCommand
    // ) {
    //     //implement business logic here:

    //     SubscriptionCancelled subscriptionCancelled = new SubscriptionCancelled(
    //         this
    //     );
    //     subscriptionCancelled.publishAfterCommit();
    // }

    // //>>> Clean Arch / Port Method

    // //<<< Clean Arch / Port Method
    // public static void checkSubscription(ReadBookPlaced readBookPlaced) {
    //     //implement business logic here:

    //     /** Example 1:  new item 
    //     Subscription subscription = new Subscription();
    //     repository().save(subscription);

    //     SubscriptionChecked subscriptionChecked = new SubscriptionChecked(subscription);
    //     subscriptionChecked.publishAfterCommit();
    //     */

    //     /** Example 2:  finding and process
        

    //     repository().findById(readBookPlaced.get???()).ifPresent(subscription->{
            
    //         subscription // do something
    //         repository().save(subscription);

    //         SubscriptionChecked subscriptionChecked = new SubscriptionChecked(subscription);
    //         subscriptionChecked.publishAfterCommit();

    //      });
    //     */

    // }
    /// >>> Clean Arch / Port Method

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subscriptionId;

    private Long userId;

    private Date startDate;

    private Date endDate;
    
    private Boolean isSubscription;

    public static SubscriptionRepository repository() {
        return SubscriptionManagementApplication.applicationContext.getBean(SubscriptionRepository.class);
    }

    public void ActivateSubscription() {
        // logic 생략
    }

    public void changeSubscription(ChangeSubscriptionCommand cmd) {
        this.userId = cmd.getUserId();
        this.isSubscription = cmd.getIsSubscription();

        SubscriptionChanged changed = new SubscriptionChanged(this);
        changed.publishAfterCommit();
    }

    public void requestSubscription(RequestSubscriptionCommand cmd) {
        this.userId = cmd.getUserId();
        this.startDate = cmd.getStartDate();
        this.endDate = cmd.getEndDate();
        this.isSubscription = true;

        SubscriptionRequested requested = new SubscriptionRequested(this);
        requested.publishAfterCommit();
    }

    public void cancelSubscription(CancelSubscriptionCommand cmd) {
        this.userId = cmd.getSubscriberId();
        this.isSubscription = false;

        SubscriptionCancelled cancelled = new SubscriptionCancelled(this);
        cancelled.publishAfterCommit();
    }

    public static void checkSubscription(ReadBookPlaced event) {
        Long userId = event.getUserId();

        java.util.Optional<Subscription> optional = repository().findByUserId(userId);

        boolean isActive = false;

        if (optional.isPresent()) {
            Subscription sub = optional.get();
            Date now = new Date();
            if (
                Boolean.TRUE.equals(sub.getIsSubscription()) &&
                sub.getStartDate() != null &&
                sub.getEndDate() != null &&
                sub.getStartDate().before(now) &&
                sub.getEndDate().after(now)
            ) {
                isActive = true;
            }
        }

        SubscriptionChecked checked = new SubscriptionChecked();
        checked.setUserId(userId);
        checked.setBookId(event.getBookId());
        checked.setIsSubscription(isActive);
        checked.publishAfterCommit();
    }

}
//>>> DDD / Aggregate Root
