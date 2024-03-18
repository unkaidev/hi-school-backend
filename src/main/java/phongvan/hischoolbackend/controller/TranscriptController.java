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
import phongvan.hischoolbackend.Service.TimeTableDetailService;
import phongvan.hischoolbackend.Service.TimeTableService;
import phongvan.hischoolbackend.Service.TranscriptService;
import phongvan.hischoolbackend.entity.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/transcript")
public class TranscriptController {
    @Autowired
    TranscriptService transcriptService;

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getTranscriptWithPagination(@RequestParam int page, @RequestParam int limit, @RequestParam int semesterId, @RequestParam int schoolClassId) {

        Page<Transcript> transcripts = null;
        try {
            transcripts = transcriptService.findPaginated(semesterId, schoolClassId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", transcripts));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("student/read")
    public ResponseEntity<MessageResponse> getTranscriptWithPaginationForStudent(@RequestParam int page, @RequestParam int limit, @RequestParam int semesterId,@RequestParam String username) {

        Page<Transcript> transcripts = null;
        try {
            transcripts = transcriptService.findPaginatedForStudent(semesterId,username, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", transcripts));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<?> UpdateTranscript(@RequestBody TranscriptRequest transcriptRequest) {
        try {
            String yearEvaluation = transcriptRequest.getYearEvaluation();
            Transcript transcriptFind = transcriptService.findById(transcriptRequest.getId());
            transcriptFind.setYearEvaluation(yearEvaluation);
            transcriptService.save(transcriptFind);

            return ResponseEntity.ok(new MessageResponse(0, "Update TimeTable successfully!", null));

        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse(-1, "Error: Create New TimeTable!", null));
        }
    }

}
