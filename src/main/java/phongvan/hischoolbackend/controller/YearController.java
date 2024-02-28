package phongvan.hischoolbackend.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.Payload.Request.YearRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.AddressRepository;
import phongvan.hischoolbackend.Service.YearService;
import phongvan.hischoolbackend.entity.Address;
import phongvan.hischoolbackend.entity.SchoolYear;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/year")
public class YearController {
    @Autowired
    YearService yearService;

    @GetMapping("/all")
    public ResponseEntity<MessageResponse> getAllYears() {

        List<SchoolYear> yearList = null;
        try {
            yearList = yearService.allYears();
        return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", yearList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getYearWithPagination(@RequestParam int page, @RequestParam int limit) {

        Page<SchoolYear> yearPage = null;
        try {
            yearPage = yearService.findPaginated(PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "GET DATA SUCCESS", yearPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @PostMapping("/create")
    public ResponseEntity<?> addYear(@Valid @RequestBody YearRequest yearRequest){
        if (yearService.existsByName(yearRequest.getName())) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1,"Error: Name is already in use!","name"));
        }
        SchoolYear year = SchoolYear.builder()
                .name(yearRequest.getName().toUpperCase())
                .build();
        yearService.updateYear(year);
        return ResponseEntity.ok(new MessageResponse(0,"Create New Year successfully!",null));

    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteYear(@PathVariable int id) {
        try {
            yearService.deleteYear(id);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Delete Year Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> updateYear(@RequestBody YearRequest yearRequest) {
        try {
            SchoolYear yearFind = yearService.findById(yearRequest.getId());
            String newName = yearRequest.getName();
            if (yearService.existsByName(newName)) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1,"Error: Name is already in use!","name"));
            }
            if(!Objects.equals(newName, "")){
                yearFind.setName(newName.toUpperCase());
            }
            yearService.updateYear(yearFind);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Update Year Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }
}
