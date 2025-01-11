package diplom.by.robot.service;

import diplom.by.robot.jwt.JwtUtil;
import diplom.by.robot.model.UserEntity;
import diplom.by.robot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

        private final UserRepository userRepository;
        private final JwtUtil jwtUtil;

        public UserEntity findUserByUsername(String username) {
            Optional<UserEntity> user = userRepository.findByUsername(username);

            if (user.isPresent()) {
                return user.get();
            }
            throw new EntityNotFoundException("юзер с таким никнеймом не найден!");
        }

        public UserEntity getUserByToken(String token) {
            return findUserByUsername(jwtUtil.validateTokenAndRetrieveClaim(token.substring(7)));
        }

        public UserEntity findUserById(Integer id) {
            Optional<UserEntity> user = userRepository.findById(id);
            if (user.isPresent()) {
                return user.get();
            }
            throw new EntityNotFoundException("cannot find user with this id!");
        }
}
