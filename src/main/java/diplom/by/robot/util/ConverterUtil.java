package diplom.by.robot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import diplom.by.robot.dto.CourseDto;
import diplom.by.robot.dto.UserDto;
import diplom.by.robot.model.CourseEntity;
import diplom.by.robot.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ConverterUtil {

        private final ObjectMapper mapper = new ObjectMapper();

        public UserDto convertUserToUserDto(UserEntity user) {
            return UserDto.builder()
                    .phone(user.getPhone())
                    .age(user.getAge())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .pathToImage(user.getPathToImage())
                    .role(user.getRole())
                    .email(user.getEmail())
                    .courses(user.getCourses()
                            .stream().map(this::convertSmallCourseToCourseDto).toList())
                    .build();
        }

        public CourseDto convertSmallCourseToCourseDto(CourseEntity course) {
           return CourseDto.builder()
                    .id(course.getId())
                    .name(course.getName())
                    .tutorUsername(course.getTutor().getUsername())
                    .build();
        }

        public CourseEntity convertCourseForCreating(CourseDto courseDto) {
            return mapper.convertValue(courseDto, CourseEntity.class);
        }
}