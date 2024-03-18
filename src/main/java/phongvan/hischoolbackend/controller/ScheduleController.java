package phongvan.hischoolbackend.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.Payload.Request.ScheduleRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.ScheduleRepository;
import phongvan.hischoolbackend.Repository.SemesterRepository;
import phongvan.hischoolbackend.Repository.YearRepository;
import phongvan.hischoolbackend.Service.ScheduleService;
import phongvan.hischoolbackend.entity.SchoolYear;
import phongvan.hischoolbackend.entity.Schedule;
import phongvan.hischoolbackend.entity.Semester;
import phongvan.hischoolbackend.entity.Subject;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;

    @Autowired
    SemesterRepository semesterRepository;

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getScheduleWithPagination(@RequestParam int page, @RequestParam int limit,@RequestParam int schoolId) {

        Page<Schedule> schedulePage = null;
        try {
            schedulePage = scheduleService.findPaginated(schoolId,PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schedulePage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("/all/{semesterId}")
    public ResponseEntity<MessageResponse> getSchedulesWithSemesterId(@PathVariable int semesterId) {
        List<Schedule> scheduleList = null;
        try {
            scheduleList = scheduleService.findAllBySemester_Id(semesterId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", scheduleList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    public boolean checkExistSchedule(String scheduleName, Integer semesterId) {
        List<Schedule> scheduleList = scheduleService.findAllByName(scheduleName);
        boolean check = false;
        for (Schedule scheduleIndex : scheduleList
        ) {
            if (scheduleIndex.getSemester().getId().equals(semesterId)) {
                check = true;
                break;
            }
        }
        return check;
    }

    @PostMapping("/create")
    public ResponseEntity<?> addSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        try {
            String scheduleName = scheduleRequest.getName().toUpperCase();
            LocalTime startTime = scheduleRequest.getStartTime();
            LocalTime endTime = scheduleRequest.getEndTime();
            Integer semesterId = scheduleRequest.getSemester().getId();
            if (checkExistSchedule(scheduleName, semesterId)) {
                return ResponseEntity.ok(new MessageResponse(-1, "Error: Schedule Is Exist!", null));
            }

            Schedule schedule = new Schedule();

            Semester semester = semesterRepository.findById(semesterId).orElse(null);
            if (semester == null) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Semester not found", null));
            }else {
            if (!scheduleName.isEmpty() && startTime != null && endTime != null) {
                schedule.setName(scheduleName);
                schedule.setStartTime(startTime);
                schedule.setEndTime(endTime);
                schedule.setSemester(semester);
                scheduleService.updateSchedule(schedule);
                return ResponseEntity.ok(new MessageResponse(0, "Create New Schedule successfully!", null));
            }}

        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse(-1, "Error: Create New Schedule!", null));
        }
        return ResponseEntity
                .ok()
                .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));

    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteSchedule(@PathVariable int id) {
        try {
            scheduleService.deleteSchedule(id);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Delete Schedule Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateSchedule( @RequestBody ScheduleRequest scheduleRequest) {
        try {
            Schedule scheduleFind = scheduleService.findById(scheduleRequest.getId());
            String newName = scheduleRequest.getName().toUpperCase();
            LocalTime startTime = scheduleRequest.getStartTime();
            LocalTime endTime = scheduleRequest.getEndTime();
            Integer semesterId = scheduleRequest.getSemester().getId();
            if (checkExistSchedule(newName, semesterId) && !newName.equals(scheduleFind.getName())) {
                return ResponseEntity.ok(new MessageResponse(-1, "Error: Schedule Is Exist!", null));
            }

            Semester semester = semesterRepository.findById(semesterId).orElse(null);
            System.out.println(semester);
            if (semester == null) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Semester not found", null));
            }else {
            if (!Objects.equals(newName, "") && startTime != null && endTime != null) {
                scheduleFind.setName(newName);
                scheduleFind.setStartTime(startTime);
                scheduleFind.setEndTime(endTime);
                scheduleFind.setSemester(semester);
                scheduleService.updateSchedule(scheduleFind);
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(0, "Update Schedule success", null));
            }
            }
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Error: Update Schedule !", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }
}
