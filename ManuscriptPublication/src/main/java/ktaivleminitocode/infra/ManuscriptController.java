package ktaivleminitocode.infra;

import java.util.Optional;
import javax.transaction.Transactional;

import ktaivleminitocode.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@Transactional
public class ManuscriptController {

    @Autowired
    ManuscriptRepository manuscriptRepository;

    @PostMapping("/manuscripts/{id}/placemanuscript")
    public ResponseEntity<?> placeManuscript(
            @PathVariable Long id
    ) {
        Optional<Manuscript> optional = manuscriptRepository.findById(id);
        if (optional.isPresent()) {
            Manuscript manuscript = optional.get();
            manuscript.placeManuscript(); // command 없이 상태만 변경
//            manuscriptRepository.save(manuscript);
            return ResponseEntity.ok(manuscript);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/manuscripts/{id}/publication-request")
    public ResponseEntity<?> requestPublication(@PathVariable Long id) {
        Optional<Manuscript> optional = manuscriptRepository.findById(id);
        if (optional.isPresent()) {
            Manuscript manuscript = optional.get();
            manuscript.requestPublication();
//            manuscriptRepository.save(manuscript);
            return ResponseEntity.ok(manuscript);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/manuscripts")
    public ResponseEntity<?> registerManuscript(@RequestBody PlaceManuscriptCommand command) {
        Manuscript manuscript = new Manuscript();
        manuscript.placeManuscript(command);
        return ResponseEntity.status(201).body(manuscript);
    }
}
