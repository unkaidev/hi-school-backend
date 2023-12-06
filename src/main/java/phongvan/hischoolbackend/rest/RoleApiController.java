package phongvan.hischoolbackend.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.entity.Role;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role")
@PreAuthorize("hasAnyAuthority('USER')")
public class RoleApiController {

    private final RoleApiService service;

    @GetMapping("/read")
    public ResponseEntity<Response> getAllRole() {
        return ResponseEntity.ok(service.getAllRole());
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createNewRole(@RequestBody List<Role> roleList) {
        return ResponseEntity.ok(service.createNewRole(roleList));
    }
    @GetMapping("/by-group/{groupId}")
    public ResponseEntity<Response> getRoleByGroup(@PathVariable int groupId){
        return ResponseEntity.ok(service.getRoleByGroup(groupId));
    }
    @PostMapping("/assign-to-group")
    public ResponseEntity<Response> assignRoleToGroup(@RequestBody Map<String,AssignRoleRequest> request){
        return ResponseEntity.ok(service.assignRoleToGroup(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteRole(@RequestBody Map<String,Integer> request){

        return ResponseEntity.ok(service.deleteRole(request));
    }

}
