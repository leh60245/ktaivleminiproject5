package ktaivleminitocode.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import javax.persistence.*;

@Entity
public class User {

    @Id
    private Long userId;

    private String name;
    private Integer points;
    private Boolean ktCustomer;
    private Date createdAt;

    // Optional subscription field (needed for getSubscription())
    private String subscription;

    protected User() {}

    public User(Long userId, String name, boolean ktCustomer) {
        this.userId = userId;
        this.name = name;
        this.points = 0;
        this.ktCustomer = ktCustomer;
        this.createdAt = new Date();
    }
    //포인트 확인로직
    public void checkPoint(SubscriptionChecked event) {
        int requiredPoints = event.getRequiredPoints();
        boolean success = this.deductPoint(requiredPoints);

        if (!success) {
            PointExhausted pointExhausted = new PointExhausted(this);
            pointExhausted.publishAfterCommit();
        } else {
            PointDeducted pointDeducted = new PointDeducted(this, requiredPoints);
            pointDeducted.publishAfterCommit();
        }
    }
    //성공실패 로직이 없어 구현
    public void placeReadBook(PlaceReadBookCommand command) {
        int cost = command.getPointCost();
        boolean success = this.deductPoint(cost);

        if (success) {
            ReadBookPlaced placed = new ReadBookPlaced(this);
            placed.setBookId(command.getBookId());
            placed.setPointsUsed(cost);
            placed.publishAfterCommit();
        } else {
            BookReadFailed failed = new BookReadFailed(this);
            failed.setBookId(command.getBookId());
            failed.setRequiredPoints(cost);
            failed.publishAfterCommit();
        }
    }
    
    public void userRegistration(UserRegistrationCommand command) {
        this.earnPoint(1000);

        if (command.isKtCustomer()) {
            this.earnPoint(5000);
        }
    }

    public void earnPoint(int amount) {
        this.points += amount;
    }

    public boolean deductPoint(int amount) {
        if (this.points < amount) {
            return false;
        }
        this.points -= amount;
        return true;
    }

    public boolean isPointExhausted() {
        return this.points == 0;
    }

    // Getter 메서드
    public Long getUserId() {
        return userId;
    }

    public Long getId() {
        return userId; // alias for use in events
    }

    public String getName() {
        return name;
    }

    public Integer getPoints() {
        return points;
    }

    public Boolean getKtCustomer() {
        return ktCustomer;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getSubscription() {
        return subscription;
    }
    public Integer getPoint() {
    return getPoints();
    }
}