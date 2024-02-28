package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date attendanceDate;

    @ManyToOne
    @JsonManagedReference
    private Teacher teacher;

    @ManyToOne
    @JsonManagedReference
    private Student student;

    @ManyToOne
    @JsonManagedReference
    private Schedule schedule;

    private boolean isPresent;
}
