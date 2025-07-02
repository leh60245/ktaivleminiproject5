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
// @RequestMapping(value="/authors")
@Transactional
public class AuthorController {

    @Autowired
    AuthorRepository authorRepository;

    @RequestMapping(
        value = "/authors/requestauthorregistration",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Author requestAuthorRegistration(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RequestAuthorRegistrationCommand requestAuthorRegistrationCommand
    ) throws Exception {
        System.out.println(
            "##### /author/requestAuthorRegistration  called #####"
        );
        Author author = new Author();
        author.requestAuthorRegistration(requestAuthorRegistrationCommand);
        authorRepository.save(author);
        return author;
    }

    @RequestMapping(
        value = "/authorsupdateauthorprofile",
        method = RequestMethod.PATCH,
        produces = "application/json;charset=UTF-8"
    )
    public Author updateAuthorProfile(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody UpdateAuthorProfileCommand updateAuthorProfileCommand
    ) throws Exception {
        System.out.println("##### /author/updateAuthorProfile  called #####");
        Author author = new Author();
        author.updateAuthorProfile(updateAuthorProfileCommand);
        authorRepository.save(author);
        return author;
    }

    @RequestMapping(
        value = "/authorsapproveauthorregistration",
        method = RequestMethod.PATCH,
        produces = "application/json;charset=UTF-8"
    )
    public Author approveAuthorRegistration(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody ApproveAuthorRegistrationCommand approveAuthorRegistrationCommand
    ) throws Exception {
        System.out.println(
            "##### /author/approveAuthorRegistration  called #####"
        );
        Author author = new Author();
        author.approveAuthorRegistration(approveAuthorRegistrationCommand);
        authorRepository.save(author);
        return author;
    }
}
//>>> Clean Arch / Inbound Adaptor
