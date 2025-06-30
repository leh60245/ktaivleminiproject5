package ktaivleminitocode.infra;

import ktaivleminitocode.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class PublicationRequestHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<PublicationRequest>> {

    @Override
    public EntityModel<PublicationRequest> process(
        EntityModel<PublicationRequest> model
    ) {
        return model;
    }
}
