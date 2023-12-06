package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_school_class")
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "_year_id")
    @JsonManagedReference
    private SchoolYear schoolYear;

    @ManyToOne
    @JoinColumn(name = "_teacher_id")
    @JsonManagedReference
    private Teacher teacher;

    @ManyToMany
            (
                    fetch = FetchType.LAZY,
                    cascade = {
                            CascadeType.PERSIST,
                            CascadeType.DETACH,
                            CascadeType.REFRESH,
                            CascadeType.MERGE})
    @JoinTable(
            name = "_class_student",
            joinColumns = @JoinColumn(name = "_class_id"),
            inverseJoinColumns = @JoinColumn(name = "_student_id")
    )
    private Collection<Student> students;

}
