package ktaivleminitocode.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktaivleminitocode.LibrarymanagementApplication;
import ktaivleminitocode.domain.BookPublished;
import ktaivleminitocode.domain.BookReadCountIncreased;
import lombok.Data;

@Entity
@Table(name = "Book_table")
@Data
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;

    private Long authorId;

    private String title;

    private String content;

    private String category;

    private String summary;

    private String coverImageUrl;

    private Integer readCount;

    private Boolean bestsellerBadge;

    private Date publishedDate;

    public static BookRepository repository() {
        BookRepository bookRepository = LibrarymanagementApplication.applicationContext.getBean(
            BookRepository.class
        );
        return bookRepository;
    }

    //<<< Clean Arch / Port Method
    public void designateBookAsBestseller(
        DesignateBookAsBestsellerCommand designateBookAsBestsellerCommand
    ) {
        //implement business logic here:

        BookDesignatedAsBestseller bookDesignatedAsBestseller = new BookDesignatedAsBestseller(
            this
        );
        bookDesignatedAsBestseller.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void registerBook(
        PublicationProcessingStarted publicationProcessingStarted
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Book book = new Book();
        repository().save(book);

        BookPublished bookPublished = new BookPublished(book);
        bookPublished.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        // if publicationProcessingStarted.chatGptId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<, Object> publicationRequestMap = mapper.convertValue(publicationProcessingStarted.getChatGptId(), Map.class);

        repository().findById(publicationProcessingStarted.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            BookPublished bookPublished = new BookPublished(book);
            bookPublished.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void readBook(PointDeducted pointDeducted) {
        //implement business logic here:

        /** Example 1:  new item 
        Book book = new Book();
        repository().save(book);

        BookReadCountIncreased bookReadCountIncreased = new BookReadCountIncreased(book);
        bookReadCountIncreased.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(pointDeducted.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            BookReadCountIncreased bookReadCountIncreased = new BookReadCountIncreased(book);
            bookReadCountIncreased.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void readBook(SubscriptionChecked subscriptionChecked) {
        //implement business logic here:

        /** Example 1:  new item 
        Book book = new Book();
        repository().save(book);

        BookReadCountIncreased bookReadCountIncreased = new BookReadCountIncreased(book);
        bookReadCountIncreased.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(subscriptionChecked.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            BookReadCountIncreased bookReadCountIncreased = new BookReadCountIncreased(book);
            bookReadCountIncreased.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
