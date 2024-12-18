package diplom.by.robot.controller;

import diplom.by.robot.dto.UserDto;
import diplom.by.robot.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

        private final AdminService adminService;

        @GetMapping("/tutors")
        public List<UserDto> tutors() {
            return adminService.getTutors();
        }

        @PatchMapping("/person-status")
        public ResponseEntity changePersonStatus(@RequestBody String username,
                                                 @RequestHeader("Authorization") String token,
                                                 @RequestParam("status") Boolean isBanned) {
            return adminService.changePersonStatus(username, isBanned, token);
        }

        @PostMapping("/tutor")
        public ResponseEntity newTutor(@ModelAttribute UserDto userDto) {
            return adminService.createTutor(userDto);
        }

        @PatchMapping("/tutor/{id}")
        public ResponseEntity updateTutor(@ModelAttribute UserDto userDto,
                                          @PathVariable("id") Integer id) {
            return adminService.updateTutor(userDto, id);
        }

        @DeleteMapping("/tutor")
        public ResponseEntity deleteTutor(@RequestBody String username) {
            return adminService.deleteTutor(username);
        }


}