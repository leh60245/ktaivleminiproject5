package ktaivleminitocode.infra;

import javax.transaction.Transactional;
import ktaivleminitocode.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
public class ManuscriptController {

    @Autowired
    ManuscriptRepository manuscriptRepository;

    /* --- 원고 등록 --- */
    @PostMapping(
        value = "/manuscripts/placemanuscript",
        produces = "application/json;charset=UTF-8")
    public Manuscript placeManuscript(
        @RequestBody PlaceManuscriptCommand cmd) {

        Manuscript m = new Manuscript();
        m.placeManuscript(cmd);
        manuscriptRepository.save(m);
        return m;
    }

    /* --- 출간 요청 --- */
    @PostMapping(
        value = "/manuscripts/publishingrequest",
        produces = "application/json;charset=UTF-8")
    public Manuscript publishingRequest(
        @RequestBody PublishingRequestCommand cmd) {

        Manuscript m = manuscriptRepository.findById(cmd.getManuscriptId())
            .orElseThrow(() -> new IllegalArgumentException(
                "해당 ID의 원고가 존재하지 않습니다."));

        m.publishingRequest();
        manuscriptRepository.save(m);
        return m;
    }
}
