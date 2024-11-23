package diplom.by.robot.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class CourseDto {

    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private String tutor;

}
