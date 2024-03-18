package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_time_table_detail")
public class TimeTableDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "_timetable_id")
    @JsonManagedReference
    private TimeTable timeTable;

    @ManyToOne
    @JoinColumn(name = "_teacher_id")
    @JsonManagedReference
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "_class_id")
    @JsonManagedReference
    private SchoolClass schoolClass;

    @ManyToOne
    @JoinColumn(name = "_schedule_id")
    @JsonManagedReference
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "_subject_id")
    private Subject subject;

    private String classComment;

}
