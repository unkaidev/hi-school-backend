package phongvan.hischoolbackend.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.Payload.Request.SchoolRequest;
import phongvan.hischoolbackend.Payload.Request.SignupRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.AddressRepository;
import phongvan.hischoolbackend.Repository.RoleRepository;
import phongvan.hischoolbackend.Service.SchoolService;
import phongvan.hischoolbackend.entity.Address;
import phongvan.hischoolbackend.entity.School;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/school")
public class SchoolController {
    @Autowired
    SchoolService schoolService;
    @Autowired
    AddressRepository addressRepository;

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getSchoolWithPagination(@RequestParam int page, @RequestParam int limit) {

        Page<School> schoolPage = null;
        try {
            schoolPage = schoolService.findPaginated(PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schoolPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @PostMapping("/create")
    public ResponseEntity<?> addSchool(@Valid @RequestBody SchoolRequest schoolRequest){
        try {
            if (schoolService.existsByName(schoolRequest.getName())) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1,"Error: Name is already in use!","name"));
            }
            Address address = schoolRequest.getAddress();
            addressRepository.save(address);
            School school = School.builder()
                    .name(schoolRequest.getName().toUpperCase())
                    .address(address)
                    .build();
            schoolService.updateSchool(school);
            return ResponseEntity.ok(new MessageResponse(0,"Create New School successfully!",null));

        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse(-1,"Error: Create New School!",null));
        }

    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteSchool(@PathVariable int id) {
        try {
            schoolService.deleteSchool(id);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Delete School Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> updateSchool(@RequestBody SchoolRequest schoolRequest) {
        try {
            School schoolFind = schoolService.findById(schoolRequest.getId());
            String newName = schoolRequest.getName();

            if (schoolService.existsByName(newName)) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1,"Error: Name is already in use!","name"));
            }

            if(!Objects.equals(newName, "")){
                schoolFind.setName(newName.toUpperCase());
            }
            Address address = schoolRequest.getAddress();
            List<Address> addressList = addressRepository.findAll();
            if(!addressList.contains(address)){
                addressRepository.save(address);
                schoolFind.setAddress(address);
            }

            schoolService.updateSchool(schoolFind);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Update School Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }
}
