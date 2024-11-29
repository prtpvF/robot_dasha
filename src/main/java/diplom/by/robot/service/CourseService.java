package diplom.by.robot.service;

import diplom.by.robot.dto.CourseDto;
import diplom.by.robot.model.CourseEntity;
import diplom.by.robot.model.UserEntity;
import diplom.by.robot.repository.CourseRepository;
import diplom.by.robot.util.ConverterUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

        private final CourseRepository courseRepository;
        private final ConverterUtil converterUtil;
        private final ImageService imageService;
        private final UserService userService;

        @Transactional
        public ResponseEntity createCourse(CourseDto courseDto) {
                CourseEntity course = converterUtil.convertCourseForCreating(courseDto);
                course.setTutor(
                        userService.findUserByUsername(
                                courseDto.getTutorUsername()
                        ));
                course.setPathToImg(imageService.saveImage(courseDto.getMultipartFile()));
                courseRepository.save(course);
                log.info("created course {}", course);
                return new ResponseEntity(OK);
        }

        @Transactional
        public ResponseEntity deleteCourse(int id) {
                Optional<CourseEntity> course = courseRepository.findById(id);

                if (course.isPresent()) {
                        courseRepository.delete(course.get());
                        return new ResponseEntity(OK);
                }
                else {
                        throw new EntityNotFoundException("cannot find course with id " + id);
                }

        }
}
