package diplom.by.robot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import diplom.by.robot.dto.CourseDto;
import diplom.by.robot.dto.UserDto;
import diplom.by.robot.exceptions.IllegalEntityException;
import diplom.by.robot.exceptions.NonUniqueEntityException;
import diplom.by.robot.jwt.JwtUtil;
import diplom.by.robot.model.CourseEntity;
import diplom.by.robot.model.UserEntity;
import diplom.by.robot.repository.CourseRepository;
import diplom.by.robot.repository.UserRepository;
import diplom.by.robot.util.ConverterUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static diplom.by.robot.util.RoleEnum.TUTOR;
import static org.springframework.http.HttpStatus.OK;


/**
 * It is a service what contains admin functionality.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

        private final UserRepository userRepository;
        private final ConverterUtil converterUtil;
        private final ImageService imageService;
        private final UserService userService;
        private final AuthService authService;
        private final JwtUtil jwtUtil;
        private final ModelMapper modelMapper;

        @Transactional
        public ResponseEntity<?> changePersonStatus(String username,
                                                 Boolean isBanned,
                                                 String token) {
            String adminUsername = jwtUtil.validateTokenAndRetrieveClaim(token);
            if(adminUsername.equals(username)) {
                throw new IllegalEntityException("вы не можете забанить самого себя!");
            }
            UserEntity user = userService.findUserByUsername(username);
            user.setIsActive(isBanned);
            userRepository.save(user);
            return new ResponseEntity<>(OK);
        }

        public ResponseEntity<?> updateTutor(UserDto userDto, int id) {
            UserEntity tutor = userService.findUserById(id);
            processUpdate(userDto);
            if(userDto.getImage() != null) {
                tutor.setPathToImage(imageService.saveImage(userDto.getImage()));
            }
            tutor.setId(id);
            modelMapper.map(userDto, tutor);
            userRepository.save(tutor);
            return new ResponseEntity<>(OK);
        }

        @Transactional
        public ResponseEntity<?> createTutor(UserDto userDto) {
           authService.checkAvailabilityForRegistration(userDto);
           UserEntity tutor = authService.convertDtoToUser(userDto);
           tutor.setRole(TUTOR.name());
            if(userDto.getImage() != null) {
                tutor.setPathToImage(imageService.saveImage(userDto.getImage()));
            }
           userRepository.save(tutor);
           return new ResponseEntity<>(OK);
        }

        public ResponseEntity<?> deleteTutor(String username) {
            UserEntity tutor = userService.findUserByUsername(username);
            imageService.deleteImage(tutor.getPathToImage());
            userRepository.delete(tutor);
            return new ResponseEntity<>(OK);
        }

        @Transactional
        public List<UserDto> getTutors() {
            List<UserEntity> users = userRepository.findAllTutors();
            return users.stream()
                    .map(converterUtil::convertUserToUserDto)
                    .collect(Collectors.toList());
        }

        public void processUpdate(UserDto userDto) {

            if (userDto.getEmail() != null) {
                 userRepository.findByEmail(userDto.getEmail()).ifPresent(
                         ex -> new NonUniqueEntityException(
                                 "пользователь с такими данными уже существует")
                 );
            }

            if (userDto.getPhone() != null) {
                userRepository.findByPhone(userDto.getPhone()).ifPresent(
                        ex -> new NonUniqueEntityException(
                                "пользователь с такими данными уже существует")
                );
            }

            if (userDto.getUsername() != null) {
                userRepository.findByUsername(userDto.getUsername()).ifPresent(
                        ex -> new NonUniqueEntityException(
                                "пользователь с такими данными уже существует")
                );
            }

        }
}