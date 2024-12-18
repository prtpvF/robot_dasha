package diplom.by.robot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import diplom.by.robot.dto.CourseDto;
import diplom.by.robot.dto.UserDto;
import diplom.by.robot.model.CourseEntity;
import diplom.by.robot.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class ConverterUtil {

        private final ModelMapper modelMapper;

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
                   .pathToFile(course.getPathToImg())
                    .tutorUsername(course.getTutor().getUsername())
                    .build();
        }

    public CourseDto convertCourseToDto(CourseEntity course) {
        return CourseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .pathToFile(course.getPathToImg())
                .startDate(course.getStartDate().toString())
                .endDate(course.getEndDate().toString())
                .tutorUsername(course.getTutor().getUsername())
                .build();
    }

        public CourseEntity convertCourseForCreating(CourseDto courseDto) {
            return modelMapper.map(courseDto, CourseEntity.class);
        }
}