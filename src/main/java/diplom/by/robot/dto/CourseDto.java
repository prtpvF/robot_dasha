package diplom.by.robot.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CourseDto {

        private int id;
        private String name;
        private String description;
        private LocalDateTime start;
        private LocalDateTime end;
        private String pathToFile;
        private MultipartFile multipartFile;
        private String tutorUsername;
        private List<UserDto> users = new ArrayList<>();
}