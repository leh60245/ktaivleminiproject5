package ktaivleminitocode.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "publicationRequests",
    path = "publicationRequests"
)
public interface PublicationRepository
    extends PagingAndSortingRepository<Publication, Long> {}
