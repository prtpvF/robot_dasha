package diplom.by.robot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String name;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;

        @ManyToOne
        @JoinColumn(name = "tutor_id", nullable = false)
        private UserEntity tutor;

        @ManyToMany
        @JoinTable(
                name = "student_course",
                joinColumns = @JoinColumn(name = "person_id"),
                inverseJoinColumns = @JoinColumn(name = "course_id"))
        private List<UserEntity> students = new ArrayList<>();

        @OneToMany(mappedBy = "courseId", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
        private List<CommentEntity> comments = new ArrayList<>();
}
