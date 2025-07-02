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
// @RequestMapping(value="/books")
@Transactional
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(
        value = "/booksdesignatebookasbestseller",
        method = RequestMethod.PATCH,
        produces = "application/json;charset=UTF-8"
    )
    public Book designateBookAsBestseller(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody DesignateBookAsBestsellerCommand designateBookAsBestsellerCommand
    ) throws Exception {
        System.out.println(
            "##### /book/designateBookAsBestseller  called #####"
        );
        Book book = bookRepository.findById(cmd.getBookId())
        .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        book.designateBookAsBestseller(designateBookAsBestsellerCommand);
        bookRepository.save(book);
        return book;
    }
}
//>>> Clean Arch / Inbound Adaptor
