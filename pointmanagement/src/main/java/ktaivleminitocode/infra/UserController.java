package ktaivleminitocode.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import ktaivleminitocode.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/users")
@Transactional
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(
        value = "/users/placereadbook",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public User placeReadBook(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody PlaceReadBookCommand placeReadBookCommand
    ) throws Exception {
        System.out.println("##### /user/placeReadBook  called #####");
        User user = new User();
        user.placeReadBook(placeReadBookCommand);
        userRepository.save(user);
        return user;
    }

    @RequestMapping(
        value = "/users/userregistration",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public User userRegistration(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody UserRegistrationCommand userRegistrationCommand
    ) throws Exception {
        System.out.println("##### /user/userRegistration  called #####");
        User user = new User();
        user.userRegistration(userRegistrationCommand);
        userRepository.save(user);
        return user;
    }
}
//>>> Clean Arch / Inbound Adaptor
