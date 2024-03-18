package phongvan.hischoolbackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.Payload.Request.ScheduleRequest;
import phongvan.hischoolbackend.Payload.Request.ScoreRequest;
import phongvan.hischoolbackend.Payload.Request.TranscriptRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.NotificationRepository;
import phongvan.hischoolbackend.Service.ScoreService;
import phongvan.hischoolbackend.Service.TranscriptService;
import phongvan.hischoolbackend.entity.Notification;
import phongvan.hischoolbackend.entity.Score;
import phongvan.hischoolbackend.entity.Transcript;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/score")
public class ScoreController {
    @Autowired
    ScoreService scoreService;
    @Autowired
    NotificationRepository notificationRepository;

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getScoreWithPagination(@RequestParam int page, @RequestParam int limit, @RequestParam int semesterId, @RequestParam int studentId) {

        Page<Score> scorePage = null;
        try {
            scorePage = scoreService.findPaginated(semesterId, studentId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", scorePage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("{username}/read")
    public ResponseEntity<MessageResponse> getScoreForTeacherWithPagination(@RequestParam int page, @RequestParam int limit, @RequestParam int semesterId, @RequestParam int studentId,@PathVariable String username) {

        Page<Score> scorePage = null;
        try {
            scorePage = scoreService.findPaginatedForTeacher(semesterId, studentId,username, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", scorePage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("/semester-evaluation")
    public ResponseEntity<MessageResponse> getSemesterEvaluation(@RequestParam int semesterId, @RequestParam int studentId) {

        String semesterEvaluation = null;
        try {
            semesterEvaluation = scoreService.getSemesterEvaluation(semesterId, studentId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", semesterEvaluation));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<?> UpdateTranscript(@RequestBody ScoreRequest scoreRequest) {
        try {
            double dailyScore = scoreRequest.getDailyScore();
            double midtermScore = scoreRequest.getMidtermScore();
            double finalScore = scoreRequest.getFinalScore();
            String subjectEvaluation = scoreRequest.getSubjectEvaluation();
            double subjectScore = scoreService.calSubjectScore(dailyScore, midtermScore, finalScore);

            Score scoreFind = scoreService.findById(scoreRequest.getId());
            scoreFind.setDailyScore(dailyScore);
            scoreFind.setMidtermScore(midtermScore);
            scoreFind.setFinalScore(finalScore);
            scoreFind.setSubjectScore(subjectScore);
            scoreFind.setSubjectEvaluation(subjectEvaluation);
            scoreService.save(scoreFind);

            Notification newNotification = Notification.builder()
                    .sender(scoreFind.getTeacher().getUser())
                    .receiver(scoreFind.getStudent().getUser())
                    .content("Có cập nhật điểm mới")
                    .timestamp(LocalDate.now())
                    .isRead(false)
                    .build();
            notificationRepository.save(newNotification);

            return ResponseEntity.ok(new MessageResponse(0, "Update Score successfully!", null));

        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse(-1, "Error: Create New TimeTable!", null));
        }
    }

}
