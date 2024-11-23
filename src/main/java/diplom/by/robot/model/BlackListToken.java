package diplom.by.robot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "token_black_list")
@Getter
@Setter
public class BlackListToken {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String token;
}
