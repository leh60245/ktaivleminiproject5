package ktaivleminitocode.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class UserRegistrationCommand {

    private Long userId;
    private String name;
    private Boolean ktCustomer;
}
