package ktaivleminitocode.domain;

import java.util.Date;
import java.util.List;
import ktaivleminitocode.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "authors", path = "authors")
public interface AuthorRepository
    extends PagingAndSortingRepository<Author, Long> {
    @Query(
        value = "select author " +
        "from Author author " +
        "where(:authorId is null or author.authorId = :authorId)"
    )
    Author authorStatistics(Long authorId);
}
