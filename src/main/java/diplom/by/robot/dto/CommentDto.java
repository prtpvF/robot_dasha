package diplom.by.robot.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class CommentDto {

    private String comment;
    private String author;
    private LocalDateTime createdAt;
}
