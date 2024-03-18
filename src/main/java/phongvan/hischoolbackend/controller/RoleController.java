package phongvan.hischoolbackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Service.RoleService;
import phongvan.hischoolbackend.entity.ERole;
import phongvan.hischoolbackend.entity.Role;
import phongvan.hischoolbackend.entity.User;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getAllRole(){
        List<Role> roles = null;
        try {
            roles = roleService.showRoleList();
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0,"Get Data Success",roles));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1,"Some Thing Went Wrong In Server",null));
        }

    }
    @GetMapping("/admin/read")
    public ResponseEntity<MessageResponse> getAllRoleForAdmin(){
        List<Role> roles = null;
        try {
            List<ERole> excludedRoles = Arrays.asList(ERole.ROLE_ADMIN, ERole.ROLE_USER, ERole.ROLE_TEACHER, ERole.ROLE_HEADTEACHER);
            roles = roleService.getRolesByNameNotIn(excludedRoles);
            return ResponseEntity.ok().body(new MessageResponse(0, "Get Data Success", roles));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/manager/read")
    public ResponseEntity<MessageResponse> getAllRoleForManager(){
        List<Role> roles = null;
        try {
            List<ERole> excludedRoles = Arrays.asList(ERole.ROLE_ADMIN, ERole.ROLE_MANAGER);
            roles = roleService.getRolesByNameNotIn(excludedRoles);
            return ResponseEntity.ok().body(new MessageResponse(0, "Get Data Success", roles));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

}
