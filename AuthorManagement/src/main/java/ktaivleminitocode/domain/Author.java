package ktaivleminitocode.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktaivleminitocode.AuthorManagementApplication;
import lombok.Data;

@Entity
@Table(name = "Author_table")
@Data
//<<< DDD / Aggregate Root
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long authorId;

    private String name;

    @Embedded // AuthorProfile은 Value Object이므로 @Embedded 어노테이션 사용
    private AuthorProfile profile;

    private AuthorStatus status;

    private Date createdAt;

    private Date updatedAt;

    // Note: 'portfolio' 필드가 AuthorProfile에도 있고 Author에도 독립적으로 있는 것으로 보입니다.
    // 만약 portfolio가 AuthorProfile의 일부라면 Author 클래스의 portfolio 필드는 제거하는 것이 좋습니다.
    // 현재는 AuthorProfile에 portfolio 필드가 있으므로, Author 클래스의 이 필드는 주석 처리하거나 삭제하는 것을 고려해 보세요.

    // private String portfolio;

    private Date approvedAt;

    public static AuthorRepository repository() {
        AuthorRepository authorRepository = AuthorManagementApplication.applicationContext.getBean(
            AuthorRepository.class
        );
        return authorRepository;
    }

    //<<< Clean Arch / Port Method
    public void requestAuthorRegistration(
        RequestAuthorRegistrationCommand requestAuthorRegistrationCommand
    ) {
        //implement business logic here:
        // 1. 커맨드로부터 받은 정보로 Author 애그리게이트의 필드 초기화
        if (this.authorId != null) { // 이미 존재하는 작가 ID라면 등록 요청 불가
            throw new IllegalStateException("Author already exists with ID: " + this.authorId);
        }

        this.name = requestAuthorRegistrationCommand.getName();
        this.profile = new AuthorProfile(
            requestAuthorRegistrationCommand.getBio(), // Assuming RequestAuthorRegistrationCommand has getBio()
            requestAuthorRegistrationCommand.getPortfolio()
        );
        this.status = AuthorStatus.REQUESTED; // 초기 상태는 '요청됨'
        this.createdAt = new Date(); // 현재 시간으로 생성 시간 기록
        this.updatedAt = new Date(); // 생성 시점과 동일하게 업데이트 시간 기록

        // 2. 비즈니스 유효성 검사 (필수 필드 확인 등)
        if (this.name == null || this.name.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty.");
        }
        if (this.profile == null || this.profile.getBio() == null || this.profile.getBio().trim().isEmpty()) {
            // Assuming bio is mandatory for profile
            throw new IllegalArgumentException("Author profile bio cannot be empty.");
        }
        // 3. 이벤트 발행

        AuthorRegistrationRequested authorRegistrationRequested = new AuthorRegistrationRequested(
            this
        );
        authorRegistrationRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void updateAuthorProfile(
        UpdateAuthorProfileCommand updateAuthorProfileCommand
    ) {
        //implement business logic here:
        // 1. 비즈니스 규칙 검증: 현재 작가 상태가 프로필 업데이트가 가능한 상태인지 확인 (예: REJECTED 상태에서는 업데이트 불가)
        if (this.status == AuthorStatus.REJECTED || this.status == AuthorStatus.INACTIVE) {
            throw new IllegalStateException("Cannot update profile for author in status: " + this.status);
        }

        // 2. 커맨드로부터 받은 정보로 Author 애그리게이트의 프로필 정보 업데이트
        // AuthorProfile은 Value Object이므로 불변성을 위해 새로운 객체를 생성하여 할당하는 것이 좋습니다.
        this.profile = new AuthorProfile(
            updateAuthorProfileCommand.getBio(), // Assuming UpdateAuthorProfileCommand has getBio()
            updateAuthorProfileCommand.getPortfolio()
        );
        this.updatedAt = new Date(); // 업데이트 시간 기록

        // 3. 이벤트 발행
        AuthorProfileUpdated authorProfileUpdated = new AuthorProfileUpdated(
            this
        );
        authorProfileUpdated.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void approveAuthorRegistration(
        ApproveAuthorRegistrationCommand approveAuthorRegistrationCommand
    ) {
        //implement business logic here:
        // 1. 비즈니스 규칙 검증: 현재 작가 상태가 승인 가능한 상태인지 확인 (반드시 REQUESTED 상태여야 함)
        if (this.status != AuthorStatus.REQUESTED) {
            throw new IllegalStateException("Cannot approve registration for author in status: " + this.status);
        }

        // 2. Author 애그리게이트의 상태를 APPROVED로 변경
        this.status = AuthorStatus.APPROVED;
        this.approvedAt = new Date(); // 승인 시간 기록
        this.updatedAt = new Date(); // 업데이트 시간 기록

        // 3. 이벤트 발행
        AuthorRegistrationApproved authorRegistrationApproved = new AuthorRegistrationApproved(
            this
        );
        authorRegistrationApproved.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
