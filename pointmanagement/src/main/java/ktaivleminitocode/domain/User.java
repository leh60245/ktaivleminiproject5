package ktaivleminitocode.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktaivleminitocode.PointmanagementApplication;
import ktaivleminitocode.domain.PointDeducted;
import ktaivleminitocode.domain.PointExhausted;
import lombok.Data;

@Entity
@Table(name = "User_table")
@Data
//<<< DDD / Aggregate Root
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String name;

    private Boolean ktCustomer;

    private Integer points;

    private Date createdAt;

    public static UserRepository repository() {
        UserRepository userRepository = PointmanagementApplication.applicationContext.getBean(
            UserRepository.class
        );
        return userRepository;
    }

    //<<< Clean Arch / Port Method
    public void placeReadBook(PlaceReadBookCommand placeReadBookCommand) {
        //implement business logic here:

        ReadBookPlaced readBookPlaced = new ReadBookPlaced(this);
        readBookPlaced.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void userRegistration(
        UserRegistrationCommand userRegistrationCommand
    ) {
        //implement business logic here:

        UserRegistered userRegistered = new UserRegistered(this);
        userRegistered.publishAfterCommit();
        SignupPointsGranted signupPointsGranted = new SignupPointsGranted(this);
        signupPointsGranted.publishAfterCommit();
        KtBonusPointsGranted ktBonusPointsGranted = new KtBonusPointsGranted(
            this
        );
        ktBonusPointsGranted.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void checkPoint(SubscriptionChecked subscriptionChecked) {
        //implement business logic here:

        /** Example 1:  new item 
        User user = new User();
        repository().save(user);

        PointDeducted pointDeducted = new PointDeducted(user);
        pointDeducted.publishAfterCommit();
        PointExhausted pointExhausted = new PointExhausted(user);
        pointExhausted.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(subscriptionChecked.get???()).ifPresent(user->{
            
            user // do something
            repository().save(user);

            PointDeducted pointDeducted = new PointDeducted(user);
            pointDeducted.publishAfterCommit();
            PointExhausted pointExhausted = new PointExhausted(user);
            pointExhausted.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
