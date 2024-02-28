package phongvan.hischoolbackend.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.Payload.Request.SemesterRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.AddressRepository;
import phongvan.hischoolbackend.Repository.SemesterRepository;
import phongvan.hischoolbackend.Repository.YearRepository;
import phongvan.hischoolbackend.Service.SemesterService;
import phongvan.hischoolbackend.Service.YearService;
import phongvan.hischoolbackend.entity.Address;
import phongvan.hischoolbackend.entity.SchoolYear;
import phongvan.hischoolbackend.entity.Semester;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/semester")
public class SemesterController {
    @Autowired
    SemesterService semesterService;
    @Autowired
    YearRepository yearRepository;

    @GetMapping("/all")
    public ResponseEntity<MessageResponse> getAllSemesters() {

        List<Semester> semesterList = null;
        try {
            semesterList = semesterService.allSemesters();
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", semesterList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getSemesterWithPagination(@RequestParam int page, @RequestParam int limit) {

        Page<Semester> semesterPage = null;
        try {
            semesterPage = semesterService.findPaginated(PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", semesterPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    public boolean checkExistSemester(String semesterName, String yearName) {
        List<Semester> semesterList = semesterService.findAllByName(semesterName);
        boolean check = false;
        for (Semester semesterIndex : semesterList
        ) {
            if (semesterIndex.getSchoolYear().getName().equals(yearName)) {
                check = true;
                break;
            }
        }
        return check;
    }

    @PostMapping("/create")
    public ResponseEntity<?> addSemester(@Valid @RequestBody SemesterRequest semesterRequest) {
        try {
            String semesterName = semesterRequest.getName().toUpperCase();
            String yearName = semesterRequest.getSchoolYear().getName();

            SchoolYear schoolYear = yearRepository.findByName(yearName).orElse(null);
            if (schoolYear == null) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: School year not found", null));
            }

            if ( checkExistSemester(semesterName, yearName)) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Semester already exists for the same school year", null));
            }
            if (!semesterName.isEmpty()) {
                Semester semester = Semester.builder()
                        .name(semesterName)
                        .schoolYear(schoolYear)
                        .build();
                semesterService.updateSemester(semester);
                return ResponseEntity.ok(new MessageResponse(0, "Create New Semester successfully!", null));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Error: Create Semester", null));
        }
        return ResponseEntity
                .ok()
                .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
    }


    @PostMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteSemester(@PathVariable int id) {
        try {
            semesterService.deleteSemester(id);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Delete Semester Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateSemester(@RequestBody SemesterRequest semesterRequest) {
        try {
            Semester semesterFind = semesterService.findById(semesterRequest.getId());
            String schoolYearName = semesterRequest.getSchoolYear().getName();
            String newName = semesterRequest.getName().toUpperCase();

            if (newName.isEmpty()) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Semester name cannot be empty", null));
            }

            if ( checkExistSemester(newName, schoolYearName)) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Semester already exists for the same school year", null));
            }

            SchoolYear schoolYear = yearRepository.findByName(schoolYearName).get();
            semesterFind.setName(newName);
            semesterFind.setSchoolYear(schoolYear);
            semesterService.updateSemester(semesterFind);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Update Semester Success", null));


        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

}
