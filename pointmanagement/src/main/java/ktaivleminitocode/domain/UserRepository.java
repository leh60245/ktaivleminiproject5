package ktaivleminitocode.domain;

import java.util.Date;
import java.util.List;
import ktaivleminitocode.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Query(
        value = "select user " +
        "from User user " +
        "where(:userId is null or user.userId = :userId)"
    )
    User pointExhaustedStatus(Long userId);
}
