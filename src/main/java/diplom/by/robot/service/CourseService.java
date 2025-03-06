package diplom.by.robot.service;

import diplom.by.robot.dto.CourseDto;
import diplom.by.robot.dto.UserDto;
import diplom.by.robot.jwt.JwtUtil;
import diplom.by.robot.model.CourseEntity;
import diplom.by.robot.model.UserEntity;
import diplom.by.robot.repository.CourseRepository;
import diplom.by.robot.util.ConverterUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

        private final CourseRepository courseRepository;
        private final ConverterUtil converterUtil;
        private final JwtUtil jwtUtil;
        private final ImageService imageService;
        private final UserService userService;

        @Value("${excel_path}")
        private String excelPath;

        @Transactional
        public ResponseEntity createCourse(CourseDto courseDto, String token) {
                processCourseCreating(courseDto);
                CourseEntity course = converterUtil.convertCourseForCreating(courseDto);
                UserEntity user = userService.getUserByToken(token);
                course.setTutor(
                        user
                        );
                course.setEndDate(courseDto.getEnd().toLocalDateTime());
                course.setStartDate(courseDto.getStart().toLocalDateTime());
                course.setPathToImg(imageService.saveImage(courseDto.getImage()));
                courseRepository.save(course);
                log.info("created course {}", course);
                return new ResponseEntity(OK);
        }

        public void saveCoursesInXml() {
                List<CourseDto> courses = courseRepository.findAll()
                        .stream()
                        .map(converterUtil::convertCourseToDto)
                        .toList();

                try (Workbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("Courses");

                        // Создаем заголовки столбцов
                        Row headerRow = sheet.createRow(0);
                        String[] columns = {"Название", "Начало", "Конец", "Описание", "Преподаватель"};
                        for (int i = 0; i < columns.length; i++) {
                                Cell cell = headerRow.createCell(i);
                                cell.setCellValue(columns[i]);
                        }

                        int rowNum = 1;
                        for (CourseDto course : courses) {
                                Row row = sheet.createRow(rowNum++);
                                row.createCell(0).setCellValue(course.getName());
                                row.createCell(1).setCellValue(course.getStartDate());
                                row.createCell(2).setCellValue(course.getEndDate());
                                row.createCell(3).setCellValue(course.getDescription());
                                row.createCell(4).setCellValue(course.getTutorUsername());
                        }

                        for (int i = 0; i < columns.length; i++) {
                                sheet.autoSizeColumn(i);
                        }

                        try (FileOutputStream fileOut = new FileOutputStream(excelPath + "courses.xlsx")) {
                                workbook.write(fileOut);
                        }

                        System.out.println("Excel файл успешно создан!");
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }

        public ResponseEntity<CourseDto> updateCourse(CourseDto courseDto, Integer courseId) {
                CourseEntity course = getCourseEntityById(courseId);
                course.setDescription(courseDto.getDescription());
                course.setStartDate(courseDto.getStart().toLocalDateTime());
                course.setEndDate(courseDto.getEnd().toLocalDateTime());
                courseRepository.save(course);
                return new ResponseEntity<CourseDto>(courseDto, HttpStatus.OK);
        }

        public List<UserDto> getAllCourseParticipant(Integer courseId) {
                CourseEntity course = getCourseEntityById(courseId);
                return course.getStudents().stream()
                        .map(converterUtil::convertCourseParticipantToUserDto)
                        .toList();
        }

        public List<CourseDto> getAll() {
               List<CourseEntity> courseEntities = courseRepository.findAll();
               List<CourseDto> dtoList = new ArrayList<>();

               for (CourseEntity courseEntity : courseEntities) {
                       dtoList.add(converterUtil.convertCourseToDto(courseEntity));
               }
               return dtoList;
        }

        public CourseDto getCourseDtoById(Integer id) {
                CourseEntity course = courseRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Курс не найден"));
                return converterUtil.convertCourseToDto(course);
        }

        public CourseEntity getCourseEntityById(Integer id) {
               return courseRepository.findById(id)
                       .orElseThrow(() -> new EntityNotFoundException("Курс не найден"));
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