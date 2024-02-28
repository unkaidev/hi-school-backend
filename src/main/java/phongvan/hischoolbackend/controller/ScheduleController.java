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
import phongvan.hischoolbackend.Repository.YearRepository;
import phongvan.hischoolbackend.Service.ScheduleService;
import phongvan.hischoolbackend.entity.SchoolYear;
import phongvan.hischoolbackend.entity.Schedule;

import java.time.LocalTime;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    YearRepository scheduleRepository;

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getScheduleWithPagination(@RequestParam int page, @RequestParam int limit) {

        Page<Schedule> schedulePage = null;
        try {
            schedulePage = scheduleService.findPaginated(PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", schedulePage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @PostMapping("/create")
    public ResponseEntity<?> addSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        try {
            String scheduleName = scheduleRequest.getScheduleName();
            if (scheduleService.existsByName(scheduleName)) {
                return ResponseEntity.ok(new MessageResponse(-1, "Error: Schedule Is Exist!", null));
            }
            LocalTime startTime = scheduleRequest.getStartTime();
            LocalTime endTime = scheduleRequest.getEndTime();
            if ((!scheduleName.isEmpty() && startTime != null && endTime != null)) {
                Schedule schedule = Schedule.builder()
                        .scheduleName(scheduleName.toUpperCase())
                        .startTime(startTime)
                        .endTime(endTime)
                        .build();
                scheduleService.updateSchedule(schedule);
                return ResponseEntity.ok(new MessageResponse(0, "Create New Schedule successfully!", null));
            }
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
    public ResponseEntity<MessageResponse> updateSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        try {
            Schedule scheduleFind = scheduleService.findById(scheduleRequest.getId());
            String newName = scheduleRequest.getScheduleName();
            LocalTime startTime = scheduleRequest.getStartTime();
            LocalTime endTime = scheduleRequest.getEndTime();

            if (scheduleService.existsByName(newName)) {
                return ResponseEntity.ok(new MessageResponse(-1, "Error: Schedule Is Exist!", null));
            }

            if (!Objects.equals(newName, "") && startTime != null && endTime != null) {
                scheduleFind.setScheduleName(newName.toUpperCase());
                scheduleFind.setStartTime(startTime);
                scheduleFind.setEndTime(endTime);
                scheduleService.updateSchedule(scheduleFind);
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(0, "UUpdate Schedule success", null));
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
