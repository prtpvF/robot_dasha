package diplom.by.robot.controller;

import diplom.by.robot.dto.ParticipationRequestDto;
import diplom.by.robot.facade.ParticipationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller contains endpoint what connected with participation requests' business logic
 * Make with REST architecture
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/participation")
public class ParticipationController {

    private final ParticipationFacade participationFacade;;

    @PostMapping("/{id}")
    public ResponseEntity<?> createParticipation(@PathVariable Integer id,
                                                 @RequestHeader("Authorization") String token) {
        return participationFacade.createParticipationRequest(token, id);
    }

    @GetMapping("/byTutor")
    public List<ParticipationRequestDto> getTutorsRequests(@RequestHeader("Authorization") String token) {
        return participationFacade.getAllByTutor(token);
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<?> acceptRequest(@PathVariable("id") Integer requestId,
                                           @RequestHeader("Authorization") String token) {
        return participationFacade.acceptRequest(requestId, token);
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectRequest(@PathVariable("id") Integer requestId,
                                           @RequestHeader("Authorization") String token) {
        return participationFacade.rejectRequest(requestId, token);
    }
}
