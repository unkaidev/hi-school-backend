package phongvan.hischoolbackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.Payload.Request.UserRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.NotificationRepository;
import phongvan.hischoolbackend.Repository.RoleRepository;
import phongvan.hischoolbackend.Repository.SchoolRepository;
import phongvan.hischoolbackend.Service.UserService;
import phongvan.hischoolbackend.entity.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    NotificationRepository notificationRepository;

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getUserWithPagination(@RequestParam int page, @RequestParam int limit) {

        Page<User> userPage = null;
        try {
            userPage = userService.findPaginated(PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "GET DATA SUCCESS", userPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "SOMETHING WENT WRONG IN SERVER", null));
        }

    }
    @GetMapping("/user")
    public ResponseEntity<MessageResponse> getUserDetail(@RequestParam String username) {

        Object userDetail = null;
        try {
            userDetail = userService.findUserByUsername(username);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "GET DATA SUCCESS", userDetail));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "SOMETHING WENT WRONG IN SERVER", null));
        }

    }
    @GetMapping("/count-notification")
    public ResponseEntity<MessageResponse> countNumberNotificationsUnRead(@RequestParam String username) {

        Integer number = 0;
        try {
            number = userService.countNumberNotificationsUnRead(username);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "GET DATA SUCCESS", number));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "SOMETHING WENT WRONG IN SERVER", null));
        }

    }
    @GetMapping("/all-notification")
    public ResponseEntity<MessageResponse> getAllNotificationsByUsernamePagination(@RequestParam int page, @RequestParam int limit, @RequestParam String username) {

        Page <Notification> notifications= null;
        try {
            notifications = userService.findAllNotificationsPaginated(username,PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "GET DATA SUCCESS", notifications));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "SOMETHING WENT WRONG IN SERVER", null));
        }

    }
    @GetMapping("/read/managers")
    public ResponseEntity<MessageResponse> getUsersWithManagerRole(@RequestParam int page, @RequestParam int limit) {
        Page<User> userPage = null;
        try {
            userPage = userService.findPaginatedUsersWithManagerRole(PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity.ok().body(new MessageResponse(0, "GET MANAGER USERS SUCCESS", userPage));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new MessageResponse(-1, "SOMETHING WENT WRONG IN SERVER", null));
        }
    }

    @GetMapping("/read/nonManagers")
    public ResponseEntity<MessageResponse> getUsersWithoutManagerAndAdmin(@RequestParam int page, @RequestParam int limit, @RequestParam Integer schoolId) {
        Page<User> userPage = null;
        try {
            userPage = userService.findPaginatedUsersWithoutManagerAndAdminAndSchool(schoolId,PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity.ok().body(new MessageResponse(0, "GET NON-MANAGER AND NON-ADMIN USERS SUCCESS", userPage));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new MessageResponse(-1, "SOMETHING WENT WRONG IN SERVER", null));
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "DELETE USER SUCCESS", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "SOMETHING WENT WRONG IN SERVER", null));
        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<MessageResponse> changeUserPassword(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");
            String reNewPassword = request.get("reNewPassword");

            String messenger = userService.changePassword(username,oldPassword,newPassword,reNewPassword);

            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, null, messenger));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "SOMETHING WENT WRONG IN SERVER", null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> updateUser(@RequestBody UserRequest userRequest) {
        try {
            User userFind = userService.anUser(userRequest.getUsername());
            String gender = userRequest.getGender();
            if (gender.isEmpty() || gender.isBlank()) {
                gender = "Nam";
            }
            userFind.setGender(gender);
            Set<Role> newRoles = new HashSet<>();
            Role newRole = roleRepository.getById(userRequest.getRoleId());
            newRoles.add(newRole);

            Integer newSchoolId = userRequest.getSchoolId();
            School newSchool = schoolRepository.getById(newSchoolId);
            userFind.setSchool(newSchool);

            String newPhone = userRequest.getPhone();
            if (userService.existsByPhone(newPhone) && !Objects.equals(newPhone, userFind.getPhone())) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1,"Error: Phone is already in use!","phone"));
            }
            userFind.setPhone(newPhone);
            if (!userFind.getRoles().equals(newRoles)) {
                userFind.setRoles(newRoles);
            }
            userService.updateUser(userFind);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "UPDATE USER SUCCESS", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "SOMETHING WENT WRONG IN SERVER", null));
        }
    }

    @PostMapping("/notification/change-status/{id}")
    public ResponseEntity<MessageResponse> changeStatus(@PathVariable int id) {
        try {
            Notification notification = notificationRepository.findById(id).orElse(null);
            if(notification!=null){
                boolean status = notification.isRead();
                notification.setRead(!status);
                notificationRepository.save(notification);
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(0, "Change status Success", null));
            }
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Notification is null", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }
}
