package diplom.by.robot.service;

import diplom.by.robot.dto.ParticipationRequestDto;
import diplom.by.robot.model.CourseEntity;
import diplom.by.robot.model.ParticipationRequestEntity;
import diplom.by.robot.model.UserEntity;
import diplom.by.robot.repository.CourseRepository;
import diplom.by.robot.repository.ParticipationRequestRepository;
import diplom.by.robot.repository.UserRepository;
import diplom.by.robot.util.ConverterUtil;
import diplom.by.robot.util.ParticipationStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipationService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ConverterUtil converterUtil;

    public ResponseEntity<?> sendRequestToParticipate(UserEntity user,
                                                      CourseEntity course) {
        ParticipationRequestEntity request = new ParticipationRequestEntity();
        request.setAuthor(user);
        request.setCourse(course);
        request.setStatus(ParticipationStatus.IN_PROCESS.name());
        participationRequestRepository.save(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ParticipationRequestEntity getRequestEntityById(Integer id) {
        return participationRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ошибка поиска запроса"));
    }

    @Transactional
    public ResponseEntity<?> acceptRequest(Integer requestId, UserEntity tutor) {
        ParticipationRequestEntity request = participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("запрос не найден"));
        if(request.getCourse().getTutor().getUsername().equals(tutor.getUsername())) {
            CourseEntity course = request.getCourse();
            course.addStudent(request.getAuthor());
            request.getAuthor().addCourse(course);
            userRepository.save(request.getAuthor());
            courseRepository.save(course);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> declineRequest(Integer requestId, UserEntity tutor) {
        ParticipationRequestEntity request = getRequestEntityById(requestId);

        if(request.getCourse().getTutor().getUsername().equals(tutor.getUsername())) {
            participationRequestRepository.delete(request);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new IllegalArgumentException("Вы не преподователь на данном курсе!");
    }

    public List<ParticipationRequestDto> getAllTutorsParticipationRequests(UserEntity organizer) {
        List<ParticipationRequestDto> requestsDto = new ArrayList<>();
        List<CourseEntity> courses = organizer.getCourses();
        participationRequestRepository.findAll().forEach(request -> {
            if (courses.contains(request.getCourse())) {
                requestsDto.add(
                        converterUtil.participationRequestToDto(request)
                );
            }
        });
        return requestsDto;
    }
}
