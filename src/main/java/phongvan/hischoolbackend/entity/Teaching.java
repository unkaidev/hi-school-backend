package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_teaching")
public class Teaching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date teachingDate;

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

    private String classComments;
}
