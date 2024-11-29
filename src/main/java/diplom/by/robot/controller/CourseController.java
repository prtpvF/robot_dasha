package diplom.by.robot.controller;

import diplom.by.robot.dto.CourseDto;
import diplom.by.robot.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

        private final CourseService courseService;

        @PostMapping("/")
        public ResponseEntity createCourse(@ModelAttribute CourseDto courseDto) {
            return courseService.createCourse(courseDto);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity deleteCourse(@PathVariable("id") int id) {
           return courseService.deleteCourse(id);
        }
}