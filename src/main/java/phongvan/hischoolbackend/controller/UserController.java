package phongvan.hischoolbackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.Payload.Request.UserRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.RoleRepository;
import phongvan.hischoolbackend.Service.UserService;
import phongvan.hischoolbackend.entity.Role;
import phongvan.hischoolbackend.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;
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

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> updateUser(@RequestBody UserRequest userRequest) {
        try {
            User userFind = userService.anUser(userRequest.getUsername()).get();
            userFind.setGender(userRequest.getGender());
            Set<Role> newRoles = new HashSet<>();
            Role newRole = roleRepository.getById(userRequest.getRoleId());
            newRoles.add(newRole);
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
}
