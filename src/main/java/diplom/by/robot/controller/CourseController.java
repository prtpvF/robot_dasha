package diplom.by.robot.controller;

import diplom.by.robot.dto.CourseDto;
import diplom.by.robot.dto.UserDto;
import diplom.by.robot.facade.CourseFacade;
import diplom.by.robot.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller contains endpoint what connected with courses' business logic
 * Make with REST architecture
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

        private final CourseService courseService;
        private final CourseFacade courseFacade;

        @PostMapping("/new")
        public ResponseEntity<?> createCourse(@ModelAttribute CourseDto courseDto,
                                           @RequestHeader("Authorization")String token) {
            return courseService.createCourse(courseDto, token);
        }

        @GetMapping("/participant/{id}")
        public List<UserDto> getCourseParticipants(@PathVariable("id") Integer courseId) {
            return courseService.getAllCourseParticipant(courseId);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteCourse(@PathVariable("id") int id) {
           return courseFacade.deleteCourse(id);
        }

        @PostMapping("/{id}")
        public ResponseEntity<?> updateCourse(@PathVariable("id") int id, @RequestBody CourseDto courseDto) {
            return courseService.updateCourse(courseDto, id);
        }

        @PostMapping("/import")
        public ResponseEntity<CourseDto> importCourse() {
            courseService.saveCoursesInXml();

            return new ResponseEntity<CourseDto>(new CourseDto(), HttpStatus.OK);
        }

        @GetMapping("/all")
        public List<CourseDto> getAllCourses() {
            return courseService.getAll();
        }
}