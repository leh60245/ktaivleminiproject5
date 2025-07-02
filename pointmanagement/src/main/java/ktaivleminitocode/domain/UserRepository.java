package ktaivleminitocode.domain;

import java.util.Date;
import java.util.List;
import ktaivleminitocode.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.query.Param;
//쿼리 수정
//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE (:userId IS NULL OR u.id = :userId)")
    User pointExhaustedStatus(@Param("userId") Long userId);
}

