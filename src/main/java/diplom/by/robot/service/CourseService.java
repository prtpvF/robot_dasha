package diplom.by.robot.service;

import diplom.by.robot.dto.CourseDto;
import diplom.by.robot.jwt.JwtUtil;
import diplom.by.robot.model.CourseEntity;
import diplom.by.robot.repository.CourseRepository;
import diplom.by.robot.util.ConverterUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

        private final CourseRepository courseRepository;
        private final ConverterUtil converterUtil;
        private final JwtUtil jwtUtil;
        private final ImageService imageService;
        private final UserService userService;

        @Transactional
        public ResponseEntity createCourse(CourseDto courseDto, String token) {
                processCourseCreating(courseDto);
                CourseEntity course = converterUtil.convertCourseForCreating(courseDto);
                course.setTutor(
                        userService.findUserByUsername(
                                jwtUtil.validateTokenAndRetrieveClaim(token.substring(7))
                        ));
                course.setEndDate(courseDto.getEnd().toLocalDateTime());
                course.setStartDate(courseDto.getStart().toLocalDateTime());
                course.setPathToImg(imageService.saveImage(courseDto.getImage()));
                courseRepository.save(course);
                log.info("created course {}", course);
                return new ResponseEntity(OK);
        }

        public List<CourseDto> getAll() {
               List<CourseEntity> courseEntities = courseRepository.findAll();
               List<CourseDto> dtoList = new ArrayList<>();

               for (CourseEntity courseEntity : courseEntities) {
                       dtoList.add(converterUtil.convertCourseToDto(courseEntity));
               }
               return dtoList;
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

        private void processCourseCreating(CourseDto courseDto) {
                LocalDateTime startDate = courseDto.getStart().toLocalDateTime();
                LocalDateTime endDate = courseDto.getEnd().toLocalDateTime();

                if (endDate.isBefore(startDate) ||
                        startDate.equals(endDate) ||
                        startDate.isBefore(LocalDateTime.now())) {
                        throw new IllegalArgumentException("вы передали неверное время!");
                }
        }
}
