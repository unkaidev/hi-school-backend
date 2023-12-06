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
@Table(name = "_score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "_subject_id")
    @JsonManagedReference
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "_student_id")
    @JsonManagedReference
    private Student student;

    @ManyToOne
    @JoinColumn(name = "_semester_id")
    @JsonManagedReference
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "teacher_assignment_id")
    @JsonManagedReference
    private TeacherAssignment teacherAssignment;

    private Double dailyScore;
    private Double midtermScore;
    private Double finalScore;
    private String subjectEvaluation;
}
