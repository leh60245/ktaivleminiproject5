package ktaivleminitocode.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import ktaivleminitocode.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Transactional
public class UserController {

    @Autowired
    UserRepository userRepository;

    /**
     * 사용자가 책을 읽는 요청 (포인트 차감)
     */
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
        System.out.println("##### /users/placereadbook called #####");

        // 🔸 기존 사용자 조회
        User user = userRepository
            .findById(placeReadBookCommand.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.placeReadBook(placeReadBookCommand);
        userRepository.save(user);

        return user;
    }

    /**
     * 신규 사용자 등록 요청 (포인트 지급 포함)
     */
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
        System.out.println("##### /users/userregistration called #####");

        // 🔸 생성자에 필요한 필드 직접 전달
        User user = new User(
            userRegistrationCommand.getUserId(),
            userRegistrationCommand.getUserName(),
            userRegistrationCommand.isKtCustomer()
        );

        // 포인트 지급 로직 실행
        user.userRegistration(userRegistrationCommand);

        userRepository.save(user);
        return user;
    }
}
