package ktaivleminitocode.infra;

import ktaivleminitocode.domain.Publication;
import ktaivleminitocode.domain.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/publicationRequests")
@Transactional
public class PublicationController {

    @Autowired
    PublicationRepository publicationRepository;

    /**
     * publicationRequestId로 Publication 객체를 조회하는 GET API
     * 예: GET /publicationRequests/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublication(@PathVariable Long id) {
        Optional<Publication> publication = publicationRepository.findById(id);
        return publication
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
