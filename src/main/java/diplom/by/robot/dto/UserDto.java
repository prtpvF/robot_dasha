package diplom.by.robot.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDto {

        private String username;
        private String firstName;
        private String password;
        private String lastName;
        private String email;
        private String phone;
        private String role;
        private Boolean isActive;
        private Integer age;
}