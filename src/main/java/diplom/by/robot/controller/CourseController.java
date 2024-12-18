package diplom.by.robot.controller;

import diplom.by.robot.dto.CourseDto;
import diplom.by.robot.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")

public class CourseController {

        private final CourseService courseService;

        @PostMapping("/new")
        public ResponseEntity createCourse(@ModelAttribute CourseDto courseDto,
                                           @RequestHeader("Authorization")String token) {
            return courseService.createCourse(courseDto, token);
        }

//        @DeleteMapping("/{id}")
//        public ResponseEntity deleteCourse(@PathVariable("id") int id) {
//           return courseService.deleteCourse(id);
//        }

        @GetMapping("/all")
        public List<CourseDto> getAllCourses() {
            return courseService.getAll();
        }
}