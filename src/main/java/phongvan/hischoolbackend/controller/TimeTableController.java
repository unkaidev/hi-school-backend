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
import phongvan.hischoolbackend.Service.SemesterService;
import phongvan.hischoolbackend.Service.TimeTableDetailService;
import phongvan.hischoolbackend.Service.TimeTableService;
import phongvan.hischoolbackend.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/timeTable")
public class TimeTableController {
    @Autowired
    TimeTableDetailService tTDService;
    @Autowired
    TimeTableService timeTableService;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    ScoreRepository scoreRepository;
    @Autowired
    TeacherAssignmentRepository tARepository;
    @Autowired
    NotificationRepository notificationRepository;


    @GetMapping("/all-week/{semesterId}")
    public ResponseEntity<MessageResponse> getTimeWeekWithSemesterId(@PathVariable int semesterId) {
        Set<String> timeWeekSet = null;
        try {
            timeWeekSet = timeTableService.findAllWeekBySemester_Id(semesterId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", timeWeekSet));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/all-day/{semesterId}")
    public ResponseEntity<MessageResponse> getTimeDayWithSemesterId(@PathVariable int semesterId) {
        Set<String> timeDaySet = null;
        try {
            timeDaySet = timeTableService.findAllDayBySemester_Id(semesterId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", timeDaySet));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/all/{semesterId}")
    public ResponseEntity<MessageResponse> getTimeTableWithSemesterId(@PathVariable int semesterId) {
        List<TimeTable> timeTableList = null;
        try {
            timeTableList = timeTableService.findAllBySemester_Id(semesterId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", timeTableList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getTimeTableWithPagination(@RequestParam int page, @RequestParam int limit, @RequestParam int semesterId, @RequestParam int schoolClassId) {

        Page<TimeTableDetail> timeTableDetails = null;
        try {
            timeTableDetails = tTDService.findPaginated(semesterId, schoolClassId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", timeTableDetails));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/{username}/all-for-teacher")
    public ResponseEntity<MessageResponse> getAllForTeacher(@PathVariable String username) {

        List<TimeTableDetail> timeTableDetails = null;
        try {
            timeTableDetails = tTDService.findALlByTeacher(username);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", timeTableDetails));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/{username}/all-for-student")
    public ResponseEntity<MessageResponse> getAllForStudent(@PathVariable String username) {

        List<TimeTableDetail> timeTableDetails = null;
        try {
            timeTableDetails = tTDService.findAllByStudent(username);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", timeTableDetails));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
@Transactional
    @PostMapping("/create")
    public ResponseEntity<?> addTimeTable(@Valid @RequestBody TimeTableRequest timeTableRequest) {
        try {
            String classComment = timeTableRequest.getClassComment();
            TeacherRequest teacherRequest = timeTableRequest.getTeacher();
            SchoolClassRequest schoolClassRequest = timeTableRequest.getSchoolClass();
            SubjectRequest subjectRequest = timeTableRequest.getSubject();
            List<ScheduleRequest> schedules = timeTableRequest.getSchedules();
            List<TimeDateRequest> timeWeeksRequests = timeTableRequest.getTimeWeeks();
            List<TimeDateRequest> timeDaysRequests = timeTableRequest.getTimeDays();

            Set<String> study_weeks = new TreeSet<>();
            for (TimeDateRequest timeDateRequest : timeWeeksRequests
            ) {
                study_weeks.add(timeDateRequest.getStudyWeek());
            }
            Set<String> study_days = new TreeSet<>();
            for (TimeDateRequest timeDateRequest : timeDaysRequests
            ) {
                study_days.add(timeDateRequest.getStudyDay());
            }
            List<TimeTable> timeTableList = timeTableService.findAllByWeekAndDay(study_weeks, study_days);


            List<Schedule> scheduleList = new ArrayList<>();
            for (ScheduleRequest scheduleRequest : schedules
            ) {
                Schedule schedule = scheduleRepository.findById(scheduleRequest.getId()).orElse(null);
                scheduleList.add(schedule);
            }
            Subject subject = subjectRepository.getById(subjectRequest.getId());
            Teacher teacher = teacherRepository.getById(teacherRequest.getId());
            SchoolClass schoolClass = schoolClassRepository.getById(schoolClassRequest.getId());

            List<TeacherAssignment> teacherAssignmentsToSave = new ArrayList<>();

            Map<Semester, Set<Teacher>> semesterTeacherPairs = new HashMap<>();

            for (TimeTable timeTable : timeTableList) {
                Semester semester = timeTable.getSemester();

                if (!semesterTeacherPairs.containsKey(semester) || !semesterTeacherPairs.get(semester).contains(teacher)) {
                    TeacherAssignment teacherAssignment = TeacherAssignment.builder()
                            .teacher(teacher)
                            .subject(subject)
                            .classes(Collections.singleton(schoolClass))
                            .semester(semester)
                            .build();
                    teacherAssignmentsToSave.add(teacherAssignment);

                    Notification newNotification = Notification.builder()
                            .sender(null)
                            .receiver(teacher.getUser())
                            .content("Có cập nhật lịch giảng dạy mới! ngày " + timeTable.getStudyDate())
                            .isRead(false)
                            .build();
                    notificationRepository.save(newNotification);

                    semesterTeacherPairs.computeIfAbsent(semester, k -> new HashSet<>()).add(teacher);
                }

                for (Schedule schedule : scheduleList) {
                    TimeTableDetail newTableDetail = TimeTableDetail.builder()
                            .timeTable(timeTable)
                            .classComment(classComment)
                            .schoolClass(schoolClass)
                            .teacher(teacher)
                            .subject(subject)
                            .schedule(schedule)
                            .build();
                    tTDService.save(newTableDetail);

                    Collection<Student> students = schoolClass.getStudents();

                    for (Student student : students) {
                        Attendance newAttendance = Attendance.builder()
                                .timeTable(timeTable)
                                .teacher(teacher)
                                .schedule(schedule)
                                .student(student)
                                .isPresent(true)
                                .build();
                        attendanceRepository.save(newAttendance);
                        if (!Objects.equals(teacher.getGroup(), "Ban giám hiệu")) {
                            Score existingScore = scoreRepository.findByStudentAndSubject(student, subject);
                            if (existingScore == null) {
                                Score newScore = Score.builder()
                                        .student(student)
                                        .dailyScore(0.0)
                                        .midtermScore(0.0)
                                        .finalScore(0.0)
                                        .subjectScore(0.0)
                                        .semester(timeTable.getSemester())
                                        .subject(subject)
                                        .teacher(teacher)
                                        .build();
                                scoreRepository.save(newScore);

                            }
                        }
                    }
                }
            }
            tARepository.saveAll(teacherAssignmentsToSave);

            return ResponseEntity.ok(new MessageResponse(0, "Create New TimeTable successfully!", null));

        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse(-1, "Error: Create New TimeTable!", null));
        }
    }

    @Transactional
    @PostMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteTimeDate(@PathVariable int id) {
        try {
            TimeTableDetail timeTableDetail = tTDService.ATimeTableDetail(id);
            Subject subjectInTTD = timeTableDetail.getSubject();
            Teacher teacherInTTD = timeTableDetail.getTeacher();
            List<TimeTableDetail> teacherAndSubject = tTDService.findAllByTeacherAndSubject(subjectInTTD, teacherInTTD);
            if (teacherAndSubject.size() == 1) {
                scoreRepository.deleteAllByTeacherAndSubject(teacherInTTD, subjectInTTD);
                tARepository.deleteAllByTeacherAndSubject(teacherInTTD, subjectInTTD);
            }
            List<Attendance> attendanceList = attendanceRepository.findAllByTimeTableAndTeacher(timeTableDetail.getTimeTable(), teacherInTTD);
            attendanceRepository.deleteAll(attendanceList);
            tTDService.deleteTimeDate(id);

            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Delete A TimeDate Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @Transactional
    @PostMapping("/delete-all/{timeTables}")
    public ResponseEntity<MessageResponse> deleteManyTimeDate(@PathVariable List<Integer> timeTables) {
        try {
            for (Integer id : timeTables
            ) {
                TimeTableDetail timeTableDetail = tTDService.ATimeTableDetail(id);
                Subject subjectInTTD = timeTableDetail.getSubject();
                Teacher teacherInTTD = timeTableDetail.getTeacher();
                List<TimeTableDetail> teacherAndSubject = tTDService.findAllByTeacherAndSubject(subjectInTTD, teacherInTTD);
                if (teacherAndSubject.size() == 1) {
                    scoreRepository.deleteAllByTeacherAndSubject(teacherInTTD, subjectInTTD);
                    tARepository.deleteAllByTeacherAndSubject(teacherInTTD, subjectInTTD);
                }
                List<Attendance> attendanceList = attendanceRepository.findAllByTimeTable_Id(timeTableDetail.getTimeTable().getId());
                attendanceRepository.deleteAll(attendanceList);
                tTDService.deleteTimeDate(id);
            }

            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Delete Many TimeDates Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<?> UpdateTimeTable(@RequestBody TimeTableRequest timeTableRequest) {
        try {
            String classComment = timeTableRequest.getClassComment();
            TimeTableDetail timeTableDetailFind = tTDService.findById(timeTableRequest.getId());
            timeTableDetailFind.setClassComment(classComment);
            tTDService.save(timeTableDetailFind);

            return ResponseEntity.ok(new MessageResponse(0, "Update TimeTable successfully!", null));

        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse(-1, "Error: Create New TimeTable!", null));
        }
    }

}
