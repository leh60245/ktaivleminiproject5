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

    private AuthorProfile profile;

    private AuthorStatus status;

    private Date createdAt;

    private Date updatedAt;

    private String portfolio;

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

        AuthorRegistrationApproved authorRegistrationApproved = new AuthorRegistrationApproved(
            this
        );
        authorRegistrationApproved.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
