package phongvan.hischoolbackend.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.Payload.Request.SubjectRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.SemesterRepository;
import phongvan.hischoolbackend.Repository.SubjectRepository;
import phongvan.hischoolbackend.Service.SubjectService;
import phongvan.hischoolbackend.entity.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/subject")
public class SubjectController {
    @Autowired
    SubjectService subjectService;
    @Autowired
    SemesterRepository semesterRepository;

    @GetMapping("/allName")
    public String[] getAllSubjectName() {
        ESubject[] subjectNames = ESubject.values();
        String[] names = new String[subjectNames.length];
        for (int i = 0; i < subjectNames.length; i++) {
            names[i] = subjectNames[i].getName();
        }
        return names;
    }

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getSubjectWithPagination(@RequestParam int page, @RequestParam int limit) {

        Page<Subject> subjectPage = null;
        try {
            subjectPage = subjectService.findPaginated(PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", subjectPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Something Went Wrong In Server", null));
        }

    }
    public boolean checkExistSubject(String subjectName, Integer semesterId) {
        List<Subject> subjectList = subjectService.findAllByName(subjectName);
        boolean check = false;
        for (Subject subjectIndex : subjectList
        ) {
            if (subjectIndex.getSemester().getId().equals(semesterId)) {
                check = true;
                break;
            }
        }
        return check;
    }

    @PostMapping("/create")
    public ResponseEntity<?> addSubject(@Valid @RequestBody SubjectRequest subjectRequest) {
        try {
            String subjectName = subjectRequest.getName().toUpperCase();
            Semester semester = subjectRequest.getSemester();

            if (semester == null) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Semester not found", null));
            }
            if (checkExistSubject(subjectName,semester.getId())) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Subject already exists for the same school subject", null));
            }
            if (!subjectName.isEmpty()) {
                Subject subject = Subject.builder()
                        .name(subjectName)
                        .semester(semester)
                        .build();
                subjectService.updateSubject(subject);
                return ResponseEntity.ok(new MessageResponse(0, "Create New Subject successfully!", null));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Error: Create Subject", null));
        }
        return ResponseEntity
                .ok()
                .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteSubject(@PathVariable int id) {
        try {
            subjectService.deleteSubject(id);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Delete Subject Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Something Went Wrong In Server", null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> updateSubject(@RequestBody SubjectRequest subjectRequest) {
        try {
            Subject subjectFind = subjectService.findById(subjectRequest.getId());
            Semester semester = subjectRequest.getSemester();
            String newName = subjectRequest.getName().toUpperCase();

            if (newName.isEmpty()) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Subject name cannot be empty", null));
            }

            if (checkExistSubject(newName,semester.getId())) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Subject already exists for the same school subject", null));
            }
            subjectFind.setName(newName);
            subjectFind.setSemester(semester);
            subjectService.updateSubject(subjectFind);

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
