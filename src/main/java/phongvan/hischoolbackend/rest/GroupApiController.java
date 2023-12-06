package phongvan.hischoolbackend.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
@PreAuthorize("hasAnyAuthority('USER')")
public class GroupApiController {

    private final GroupApiService service;

    @GetMapping("/read")
    public ResponseEntity<Response> getGroupMemberList(){
        return ResponseEntity.ok(service.getGroupMemberList());
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createNewGroup(@RequestBody Map<String,String>[] groupList){
        return ResponseEntity.ok(service.createNewGroup(groupList));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteRole(@RequestBody Map<String,Integer> request){

        return ResponseEntity.ok(service.deleteGroup(request));
    }
}
