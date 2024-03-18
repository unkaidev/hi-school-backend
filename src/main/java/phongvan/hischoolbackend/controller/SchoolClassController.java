package phongvan.hischoolbackend.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.Payload.Request.SchoolClassRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.RoleRepository;
import phongvan.hischoolbackend.Repository.TeacherRepository;
import phongvan.hischoolbackend.Repository.YearRepository;
import phongvan.hischoolbackend.Service.SchoolClassService;
import phongvan.hischoolbackend.entity.*;

import java.util.*;

@RestController

@RequestMapping("/api/v1/schoolClass")
public class SchoolClassController {
    @Autowired
    SchoolClassService schoolClassService;
    @Autowired
    YearRepository yearRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/allGrade")
    public String[] getAllGrade() {
        EGrade[] grades = EGrade.values();
        String[] names = new String[grades.length];
        for (int i = 0; i < grades.length; i++) {
            names[i] = grades[i].getName();
        }
        return names;
    }
    @GetMapping("/count-students")
    public ResponseEntity<MessageResponse> countStudentsInClass(@RequestParam int classId) {

        int size = 0;
        try {
            size = schoolClassService.countStudentsInClass(classId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", size));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/all")
    public ResponseEntity<MessageResponse> getAllSchoolClasses() {

        List<SchoolClass> schoolClassList = null;
        try {
            schoolClassList = schoolClassService.allSchoolClasses();
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schoolClassList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("/view/{id}")
    public ResponseEntity<MessageResponse> getASchoolClass(@PathVariable int id) {
        try {
            SchoolClass schoolClass = schoolClassService.findById(id);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schoolClass));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getSchoolClassWithPagination(@RequestParam int page, @RequestParam int limit, @RequestParam int schoolId) {

        Page<SchoolClass> schoolClassPage = null;
        try {
            schoolClassPage = schoolClassService.findPaginated(schoolId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schoolClassPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
      @GetMapping("{yearId}/all-by-grade")
    public ResponseEntity<MessageResponse> getSchoolClassWithGradeAndYearId(@RequestParam String grade,@PathVariable int yearId) {

        List<SchoolClass> schoolClassList = null;
        try {
            schoolClassList = schoolClassService.findAllBySchoolYear_IdAndGrade(yearId,grade);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schoolClassList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/all/{yearId}")
    public ResponseEntity<MessageResponse> getClassWithYearId(@PathVariable int yearId) {
        List<SchoolClass> schoolClassList = null;
        try {
            schoolClassList = schoolClassService.findAllBySchoolYear_Id(yearId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schoolClassList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
       @GetMapping("/all-for-headteacher/{username}/{semesterId}")
    public ResponseEntity<MessageResponse> findAllBySemesterIdAndHeadTeacher(@PathVariable String username,@PathVariable int semesterId) {
        List<SchoolClass> schoolClassList = null;
        try {
            schoolClassList = schoolClassService.findAllBySemesterIdAndHeadTeacher(semesterId, username);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schoolClassList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }
    @GetMapping("/head-teacher/all-by-grade/{username}/{yearId}/{grade}")
    public ResponseEntity<MessageResponse> getAllWithYearIdAndGradeForHeadTeacher(@PathVariable String username,@PathVariable int yearId,@PathVariable String grade) {
        List<SchoolClass> schoolClassList = null;
        try {
            schoolClassList = schoolClassService.findAllBySchoolYear_IdAndGradeForHeadteacher(yearId,username,grade);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schoolClassList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/student/all-by-grade/{username}/{yearId}/{grade}")
    public ResponseEntity<MessageResponse> getAllWithYearIdAndGradeForStudent(@PathVariable String username,@PathVariable int yearId,@PathVariable String grade) {
        List<SchoolClass> schoolClassList = null;
        try {
            schoolClassList = schoolClassService.findAllBySchoolYear_IdAndGradeForStudent(yearId,username,grade);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schoolClassList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }



    @GetMapping("/{username}/read")
    public ResponseEntity<MessageResponse> getClassForHeadTeacherWithPagination(@PathVariable String username,@RequestParam int page, @RequestParam int limit, @RequestParam int schoolId) {
            Page<SchoolClass> schoolClassPage = null;
            try {
                schoolClassPage = schoolClassService.findPaginatedForHeadTeacher(username,schoolId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(0, "Get Data Success", schoolClassPage));

            } catch (Exception e) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
            }


    }

    @GetMapping("/all-for-teacher/{username}/{semesterId}")
    public ResponseEntity<MessageResponse> getClassWithSemesterIdAndTeacher(@PathVariable String username,@PathVariable int semesterId) {
        List<SchoolClass> schoolClassList = null;
        try {
            schoolClassList = schoolClassService.findAllBySemester_IdAndTeacher(semesterId,username);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schoolClassList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("/all-for-student/{username}/{semesterId}")
    public ResponseEntity<MessageResponse> getClassWithSemesterIdAndStudent(@PathVariable String username,@PathVariable int semesterId) {
        Set<SchoolClass> classes = null;
        try {
            classes = schoolClassService.findAllBySemester_IdAndStudent(semesterId,username);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", classes));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("/all-in-school/{schoolId}")
    public ResponseEntity<MessageResponse> getAllClassWithSchoolId(@PathVariable int schoolId) {
        List<SchoolClass> schoolClassList = null;
        try {
            schoolClassList = schoolClassService.findAllBySchool(schoolId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schoolClassList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }


    public boolean checkExistSchoolClass(String schoolClassName, String yearName, Integer schoolId) {
        SchoolYear schoolYear = yearRepository.findByNameAndSchool_Id(yearName, schoolId).orElse(null);
        if (schoolYear == null) {
            return false;
        }
        return schoolClassService.existsByNameAndSchoolYearId(schoolClassName, schoolYear.getId());
    }

    @PostMapping("/create")
    public ResponseEntity<?> addSchoolClass(@Valid @RequestBody SchoolClassRequest schoolClassRequest) {
        try {
            String schoolClassName = schoolClassRequest.getName().toUpperCase();
            String yearName = schoolClassRequest.getSchoolYear().getName();
            String grade = schoolClassRequest.getGrade();
            Integer schoolId = Integer.valueOf(schoolClassRequest.getSchoolYear().getSchoolId());
            Integer teacherId = schoolClassRequest.getTeacher().getId();
            Teacher newTeacher = teacherRepository.getById(teacherId);
            Set<Role> teacherRoles = newTeacher.getUser().getRoles();
            Role role_headTeacher = roleRepository.findByName(ERole.ROLE_HEADTEACHER).orElse(null);
            teacherRoles.add(role_headTeacher);
            newTeacher.getUser().setRoles(teacherRoles);
            teacherRepository.save(newTeacher);

            if (checkExistSchoolClass(schoolClassName, yearName, schoolId)) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: SchoolClass already exists for the same school year", null));
            }
            SchoolClass schoolClass = new SchoolClass();
            SchoolYear schoolYear = yearRepository.findByNameAndSchool_Id(yearName, schoolId).orElse(null);
            if (schoolYear == null) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: School year not found", null));
            } else {
                if (!schoolClassName.isEmpty()) {
                    schoolClass.setName(schoolClassName);
                    schoolClass.setGrade(grade);
                    schoolClass.setSchoolYear(schoolYear);
                    schoolClass.setTeacher(newTeacher);
                    schoolClassService.updateSchoolClass(schoolClass);
                    return ResponseEntity.ok(new MessageResponse(0, "Create New SchoolClass successfully!", null));
                }
            }

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Error: Create SchoolClass", null));
        }
        return ResponseEntity
                .ok()
                .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<?> updateSchoolClass(@Valid @RequestBody SchoolClassRequest schoolClassRequest) {
        try {
            SchoolClass schoolClassFind = schoolClassService.findById(schoolClassRequest.getId());

            String schoolYearName = schoolClassRequest.getSchoolYear().getName();
            Integer schoolId = Integer.valueOf(schoolClassRequest.getSchoolYear().getSchoolId());
            String newName = schoolClassRequest.getName().toUpperCase();
            String newGrade = schoolClassRequest.getGrade();
            Role role_headTeacher = roleRepository.findByName(ERole.ROLE_HEADTEACHER).orElse(null);
            Teacher existTeacher = schoolClassFind.getTeacher();
            Set<Role> existTeacherRoles = existTeacher.getUser().getRoles();
            existTeacherRoles.remove(role_headTeacher);
            teacherRepository.save(existTeacher);
            Integer teacherId = schoolClassRequest.getTeacher().getId();
            Teacher newTeacher = teacherRepository.getById(teacherId);
            Set<Role> teacherRoles = newTeacher.getUser().getRoles();
            teacherRoles.add(role_headTeacher);
            newTeacher.getUser().setRoles(teacherRoles);
            teacherRepository.save(newTeacher);
            if (newName.isEmpty()) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: SchoolClass name cannot be empty", null));
            }

            if (checkExistSchoolClass(newName, schoolYearName, schoolId) && !Objects.equals(newName,schoolClassFind.getName())) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: SchoolClass already exists for the same school year", null));
            }

            SchoolYear schoolYear = yearRepository.findByNameAndSchool_Id(schoolYearName, schoolId).get();
            schoolClassFind.setName(newName);
            schoolClassFind.setGrade(newGrade);
            schoolClassFind.setSchoolYear(schoolYear);
            schoolClassFind.setTeacher(newTeacher);
            schoolClassService.updateSchoolClass(schoolClassFind);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Update SchoolClass Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteSchoolClass(@PathVariable int id) {
        try {
            schoolClassService.deleteSchoolClass(id);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Delete SchoolClass Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

}
