package phongvan.hischoolbackend.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.Payload.Request.*;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.*;
import phongvan.hischoolbackend.Service.AttendanceService;
import phongvan.hischoolbackend.Service.TimeTableDetailService;
import phongvan.hischoolbackend.Service.TimeTableService;
import phongvan.hischoolbackend.entity.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    NotificationRepository notificationRepository;


    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getTimeTableWithPagination(@RequestParam int page, @RequestParam int limit, @RequestParam int timeTableId,
                                                                      @RequestParam int scheduleId, @RequestParam int teacherId, @RequestParam int classId) {


        Page<Attendance> attendances = null;
        try {
            attendances = attendanceService.findPaginated(timeTableId, scheduleId, teacherId,classId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", attendances));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @PostMapping("/change-status/{id}")
    public ResponseEntity<MessageResponse> changeStatus(@PathVariable int id) {
        try {
            Attendance attendance = attendanceService.AnAttendance(id);
            boolean status = attendance.isPresent();
            attendance.setPresent(!status);
            attendanceService.save(attendance);

            Notification newNotification = Notification.builder()
                    .sender(attendance.getTeacher().getUser())
                    .receiver(attendance.getStudent().getUser())
                    .content("Cập nhật điểm danh mới ngày: " + attendance.getTimeTable().getStudyDate())
                    .isRead(false)
                    .build();
            notificationRepository.save(newNotification);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Change status Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }



}
