package ktaivleminitocode.domain;

import ktaivleminitocode.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "publicationRequests",
    path = "publicationRequests"
)
public interface PublicationRequestRepository
    extends PagingAndSortingRepository<PublicationRequest, Long> {}
