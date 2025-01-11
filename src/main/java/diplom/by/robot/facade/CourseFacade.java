package diplom.by.robot.facade;

import diplom.by.robot.dto.UserDto;
import diplom.by.robot.service.CourseService;
import diplom.by.robot.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseFacade {

        private final CourseService courseService;
        private final NotificationService notificationService;

    public ResponseEntity<?> deleteCourse(Integer id) {
        try {
            courseService.deleteCourse(id);

            List<UserDto> students = getAllCourseStudents(id);
            for (UserDto user : students) {
                notificationService.sendEmail(user.getEmail(),
                        "Курс отменен",
                        "Курс отменен");
            }

            return ResponseEntity.ok("Курс успешно отменен и уведомления отправлены.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении курса: " + e.getMessage());
        }
    }

        private List<UserDto> getAllCourseStudents(Integer id) {
            return courseService.getCourseDtoById(id).getUsers();
        }


}
