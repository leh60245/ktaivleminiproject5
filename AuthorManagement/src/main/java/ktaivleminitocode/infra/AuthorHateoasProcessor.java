package ktaivleminitocode.infra;

import ktaivleminitocode.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class AuthorHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Author>> {

    @Override
    public EntityModel<Author> process(EntityModel<Author> model) {
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/requestauthorregistration"
                )
                .withRel("requestauthorregistration")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/updateauthorprofile"
                )
                .withRel("updateauthorprofile")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/approveauthorregistration"
                )
                .withRel("approveauthorregistration")
        );

        return model;
    }
}
