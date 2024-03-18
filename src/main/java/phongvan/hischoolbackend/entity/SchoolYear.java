package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "_school_year")
public class SchoolYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "_school_id")
    private School school;

    @OneToMany(mappedBy = "schoolYear")
    @JsonIgnore
    private List<Semester> semesters;

    @Override
    public String toString() {
        return "SchoolYear{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
