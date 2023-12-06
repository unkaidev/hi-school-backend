package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_teacher_assignment")
public class TeacherAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany
    @JoinTable(
            name = "_teacher_class",
            joinColumns = @JoinColumn(name = "_assignment_id"),
            inverseJoinColumns = @JoinColumn(name = "_class_id")
    )
    private List<SchoolClass> classes;

    @ManyToOne
    @JoinColumn(name = "_teacher_id")
    @JsonManagedReference
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "_subject_id")
    @JsonManagedReference
    private Subject subject;

    @OneToMany(mappedBy = "teacherAssignment",
            cascade = CascadeType.ALL)
    private List<Score> scores;

}
