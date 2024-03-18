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
import phongvan.hischoolbackend.Repository.TimeTableRepository;
import phongvan.hischoolbackend.Repository.YearRepository;
import phongvan.hischoolbackend.Service.SemesterService;
import phongvan.hischoolbackend.Service.TimeTableService;
import phongvan.hischoolbackend.Service.YearService;
import phongvan.hischoolbackend.entity.Address;
import phongvan.hischoolbackend.entity.SchoolYear;
import phongvan.hischoolbackend.entity.Semester;
import phongvan.hischoolbackend.entity.TimeTable;

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
    @Autowired
    TimeTableService timeTableService;

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
    public ResponseEntity<MessageResponse> getSemesterWithPagination(@RequestParam int page, @RequestParam int limit, @RequestParam int schoolId) {

        Page<Semester> semesterPage = null;
        try {
            semesterPage = semesterService.findPaginated(schoolId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", semesterPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("/all/{yearId}")
    public ResponseEntity<MessageResponse> getSemesterWithYearId(@PathVariable int yearId) {
        List<Semester> semesterPage = null;
        try {
            semesterPage = semesterService.findAllBySchoolYear(yearId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", semesterPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("/all-in-school/{schoolId}")
    public ResponseEntity<MessageResponse> getSemesterWithSchoolId(@PathVariable int schoolId) {
        List<Semester> semesterPage = null;
        try {
            semesterPage = semesterService.findAllBySchoolId(schoolId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", semesterPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    public boolean checkExistSemester(String semesterName, String yearName, Integer schoolId) {
        SchoolYear schoolYear = yearRepository.findByNameAndSchool_Id(yearName, schoolId).orElse(null);
        if (schoolYear == null) {
            return false;
        }
        return semesterService.existsByNameAndSchoolYearId(semesterName, schoolYear.getId());
    }

    @PostMapping("/create")
    public ResponseEntity<?> addSemester(@Valid @RequestBody SemesterRequest semesterRequest) {
        try {
            String semesterName = semesterRequest.getName().toUpperCase();
            String yearName = semesterRequest.getSchoolYear().getName();
            Integer schoolId = Integer.valueOf(semesterRequest.getSchoolYear().getSchoolId());
            String study_period = semesterRequest.getStudy_period();
            String start_date = semesterRequest.getStart_date();
            String end_date = semesterRequest.getEnd_date();

            if (checkExistSemester(semesterName, yearName, schoolId)) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Semester already exists for the same school year", null));
            }
            Semester semester = new Semester();
            SchoolYear schoolYear = yearRepository.findByNameAndSchool_Id(yearName, schoolId).orElse(null);
            if (schoolYear == null) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: School year not found", null));
            } else {
                if (!semesterName.isEmpty()) {
                    semester.setName(semesterName);
                    semester.setSchoolYear(schoolYear);
                    semester.setStudy_period(study_period);
                    semesterService.updateSemester(semester);

                    List<TimeTable> timeTables = TimeTableService.createTimetableFromStudyPeriod(start_date,end_date,semester);
                    timeTableService.saveManyTimeTable(timeTables);

                    return ResponseEntity.ok(new MessageResponse(0, "Create New Semester successfully!", null));
                }
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

    @PutMapping("/update")
    public ResponseEntity<?> updateSemester(@RequestBody SemesterRequest semesterRequest) {
        try {
            Semester semesterFind = semesterService.findById(semesterRequest.getId());
            List<TimeTable> existTimeTables = timeTableService.findAllBySemester(semesterFind);

            String schoolYearName = semesterRequest.getSchoolYear().getName();
            String newName = semesterRequest.getName().toUpperCase();
            String new_study_period = semesterRequest.getStudy_period();
            Integer schoolId = Integer.valueOf(semesterRequest.getSchoolYear().getSchoolId());
            String new_start_date = semesterRequest.getStart_date();
            String new_end_date = semesterRequest.getEnd_date();
            if (newName.isEmpty()) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Semester name cannot be empty", null));
            }

            if (checkExistSemester(newName, schoolYearName, schoolId) && !newName.equals(semesterFind.getName())) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Semester already exists for the same school year", null));
            }

            SchoolYear schoolYear = yearRepository.findByNameAndSchool_Id(schoolYearName, schoolId).get();
            semesterFind.setName(newName);
            semesterFind.setSchoolYear(schoolYear);
            semesterFind.setStudy_period(new_study_period);
            semesterService.updateSemester(semesterFind);

            timeTableService.deleteAll(existTimeTables);
            List<TimeTable> newTimeTables = TimeTableService.createTimetableFromStudyPeriod(new_start_date,new_end_date,semesterFind);
            timeTableService.saveManyTimeTable(newTimeTables);

            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Update Semester Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteSemester(@PathVariable int id) {
        try {
            Semester semesterFind = semesterService.findById(id);
            List<TimeTable> existTimeTables = timeTableService.findAllBySemester(semesterFind);
            timeTableService.deleteAll(existTimeTables);
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

}
