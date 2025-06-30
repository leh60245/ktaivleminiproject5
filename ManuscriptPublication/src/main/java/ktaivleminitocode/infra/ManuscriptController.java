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
// @RequestMapping(value="/manuscripts")
@Transactional
public class ManuscriptController {

    @Autowired
    ManuscriptRepository manuscriptRepository;

    @RequestMapping(
        value = "/manuscripts/placemanuscript",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Manuscript placeManuscript(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody PlaceManuscriptCommand placeManuscriptCommand
    ) throws Exception {
        System.out.println("##### /manuscript/placeManuscript  called #####");
        Manuscript manuscript = new Manuscript();
        manuscript.placeManuscript(placeManuscriptCommand);
        manuscriptRepository.save(manuscript);
        return manuscript;
    }

    @RequestMapping(
        value = "/manuscripts/publishingrequest",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Manuscript publishingRequest(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody PublishingRequestCommand publishingRequestCommand
    ) throws Exception {
        System.out.println("##### /manuscript/publishingRequest  called #####");
        Manuscript manuscript = new Manuscript();
        manuscript.publishingRequest(publishingRequestCommand);
        manuscriptRepository.save(manuscript);
        return manuscript;
    }
}
//>>> Clean Arch / Inbound Adaptor
