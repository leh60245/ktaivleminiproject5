package ktaivleminitocode.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktaivleminitocode.ManuscriptPublicationApplication;
import ktaivleminitocode.domain.PublicationProcessingStarted;
import lombok.Data;

@Entity
@Table(name = "PublicationRequest_table")
@Data
//<<< DDD / Aggregate Root
public class PublicationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long publicationRequestId;

    private PublicationFormat format;

    private PublicationRequestStatus status;

    private Date publishedDate;

    private Date createdDate;

    private String category;

    private String summary;

    private String coverImageUrl;

    public static PublicationRequestRepository repository() {
        PublicationRequestRepository publicationRequestRepository = ManuscriptPublicationApplication.applicationContext.getBean(
            PublicationRequestRepository.class
        );
        return publicationRequestRepository;
    }

    //<<< Clean Arch / Port Method
    public static void publishingPrepared(
        PublishingRequested publishingRequested
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        PublicationRequest publicationRequest = new PublicationRequest();
        repository().save(publicationRequest);

        PublicationProcessingStarted publicationProcessingStarted = new PublicationProcessingStarted(publicationRequest);
        publicationProcessingStarted.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(publishingRequested.get???()).ifPresent(publicationRequest->{
            
            publicationRequest // do something
            repository().save(publicationRequest);

            PublicationProcessingStarted publicationProcessingStarted = new PublicationProcessingStarted(publicationRequest);
            publicationProcessingStarted.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
