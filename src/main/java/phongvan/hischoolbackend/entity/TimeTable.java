package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_time_table")
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String studyDay;
    private String studyDate;
    private String studyWeek;

    @ManyToOne
    @JoinColumn(name = "_semester_id")
    @JsonManagedReference
    private Semester semester;

    @Override
    public String toString() {
        return "TimeTable{" +
                "id=" + id +
                ", studyDay='" + studyDay + '\'' +
                ", studyDate='" + studyDate + '\'' +
                ", studyWeek='" + studyWeek + '\'' +
                '}';
    }
}
