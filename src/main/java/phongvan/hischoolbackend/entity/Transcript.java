package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "_transcript")
public class Transcript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String semesterEvaluation;

    @ManyToOne
            (fetch = FetchType.LAZY,
                    cascade = {
                            CascadeType.PERSIST,
                            CascadeType.DETACH,
                            CascadeType.REFRESH,
                            CascadeType.MERGE})
    @JoinColumn(name = "_student_id")
    @JsonManagedReference
    private Student student;

    @ManyToOne
    @JoinColumn(name = "_year_id")
    @JsonManagedReference
    private SchoolYear schoolYear;
}
