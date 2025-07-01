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

// 기존 속성과 이름을 유지한 채, 도메인 이벤트 적용 및 도메인 로직 정제
@Entity
public class User {

    @Id
    private Long id;

    private String name;
    private Integer point;

    private Boolean isKtCustomer;
    private Boolean subscription;

    protected User() {}

    public User(Long id, String name, boolean isKtCustomer) {
        this.id = id;
        this.name = name;
        this.point = 0;
        this.isKtCustomer = isKtCustomer;
        this.subscription = false;
    }

    // 포인트 적립 로직
    public void earnPoint(int amount) {
        this.point += amount;
    }

    // 포인트 차감 로직
    public boolean deductPoint(int amount) {
        if (this.point < amount) {
            return false;
        }
        this.point -= amount;
        return true;
    }

    // 포인트가 모두 소진됐는지 여부 확인
    public boolean isPointExhausted() {
        return this.point == 0;
    }

    public void registerSubscription() {
        this.subscription = true;
    }

    public boolean hasSubscription() {
        return Boolean.TRUE.equals(this.subscription);
    }

    public Long getId() {
        return id;
    }

    public Integer getPoint() {
        return point;
    }

    public Boolean getIsKtCustomer() {
        return isKtCustomer;
    }

    public Boolean getSubscription() {
        return subscription;
    }
}

