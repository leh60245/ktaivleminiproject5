package ktaivleminitocode.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktaivleminitocode.ManuscriptPublicationApplication;
import lombok.Data;

@Entity
@Table(name = "Manuscript_table")
@Data
//<<< DDD / Aggregate Root
public class Manuscript {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long manuscriptId;

    private String title;

    private String content;

    private ManuscriptStatus status;

    private Date createdDate;

    private Long authorId;

    public static ManuscriptRepository repository() {
        ManuscriptRepository manuscriptRepository = ManuscriptPublicationApplication.applicationContext.getBean(
            ManuscriptRepository.class
        );
        return manuscriptRepository;
    }

    //<<< Clean Arch / Port Method
    public void placeManuscript(PlaceManuscriptCommand placeManuscriptCommand) {
        //implement business logic here:

        ManuscriptPlaced manuscriptPlaced = new ManuscriptPlaced(this);
        manuscriptPlaced.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void publishingRequest(
        PublishingRequestCommand publishingRequestCommand
    ) {
        //implement business logic here:

        PublishingRequested publishingRequested = new PublishingRequested(this);
        publishingRequested.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
