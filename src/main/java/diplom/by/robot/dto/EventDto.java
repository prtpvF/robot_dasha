package diplom.by.robot.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class EventDto {

    private String name;
    private String description;
    private String pathToImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dateOfEvent;
}
