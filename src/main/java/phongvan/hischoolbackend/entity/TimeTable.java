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

    private Date studyDate;

    @ManyToOne
    @JoinColumn(name = "_semester_id")
    @JsonManagedReference
    private Semester semester;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    @JoinTable(
            name = "_timetable_schedule",
            joinColumns = @JoinColumn(name = "_timetable_id"),
            inverseJoinColumns = @JoinColumn(name = "_schedule_id")
    )
    private Collection<Schedule> schedules;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    @JoinTable(
            name = "_timetable_class",
            joinColumns = @JoinColumn(name = "_timetable_id"),
            inverseJoinColumns = @JoinColumn(name = "_class_id")
    )
    private Collection<SchoolClass> classes;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    @JoinTable(
            name = "_timetable_teacher",
            joinColumns = @JoinColumn(name = "_timetable_id"),
            inverseJoinColumns = @JoinColumn(name = "_teacher_id")
    )
    private Collection<Teacher> teachers;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    @JoinTable(
            name = "_timetable_subject",
            joinColumns = @JoinColumn(name = "_timetable_id"),
            inverseJoinColumns = @JoinColumn(name = "_subject_id")
    )
    private Collection<Subject> subjects;
}
