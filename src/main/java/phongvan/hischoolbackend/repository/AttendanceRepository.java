package phongvan.hischoolbackend.Repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.*;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findAllByTimeTable_Id(int id);

    List<Attendance> findAllByTimeTableAndTeacher(TimeTable timeTable, Teacher teacherInTTD);


    List<Attendance> findAllByStudent(Student student);

    List<Attendance> findAllByTimeTable_IdAndSchedule_IdAndTeacher_IdAndStudent_Id(int timeTableId, int scheduleId, int teacherId, int studentId, Sort id);

    Attendance findAllByStudentAndScheduleAndTeacherAndTimeTable(Student studentToRemove, Schedule schedule, Teacher teacher, TimeTable timeTable);
}
