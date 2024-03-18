package phongvan.hischoolbackend.Repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.Attendance;
import phongvan.hischoolbackend.entity.Student;
import phongvan.hischoolbackend.entity.Teacher;
import phongvan.hischoolbackend.entity.TimeTable;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findAllByTimeTable_Id(int id);

    List<Attendance> findAllByTimeTableAndTeacher(TimeTable timeTable, Teacher teacherInTTD);

    List<Attendance> findAllByTimeTable_IdAndSchedule_IdAndTeacherId(int timeTableId, int scheduleId, int teacherId, Sort id);


    List<Attendance> findAllByStudent(Student student);

    List<Attendance> findAllByTimeTable_IdAndSchedule_IdAndTeacher_IdAndStudent_Id(int timeTableId, int scheduleId, int teacherId, int studentId, Sort id);
}
