package phongvan.hischoolbackend.Payload.Request;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import phongvan.hischoolbackend.entity.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableRequest {
    private Integer id;
    private String classComment;
    private TeacherRequest teacher;
    private SchoolClassRequest schoolClass;
    private SubjectRequest subject;
    private List<ScheduleRequest> schedules;
    private ScheduleRequest schedule;
    private TimeDateRequest timeTable;
    private List<TimeDateRequest> timeWeeks;
    private List<TimeDateRequest> timeDays;

}
