package phongvan.hischoolbackend.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.*;

import java.util.List;

@Repository
public interface TimeTableDetailRepository extends JpaRepository<TimeTableDetail, Integer> {

    List<TimeTableDetail> findAllBySchoolClassAndTimeTable(SchoolClass schoolClass, TimeTable timeTable);

    List<TimeTableDetail> findAllBySchoolClass(SchoolClass schoolClass);

    List<TimeTableDetail>  findAllByTimeTable_Id(Integer id);

    List<TimeTableDetail> findAllBySubject(Subject subject);
    List<TimeTableDetail> findAllByTeacher(Teacher teacher);


    List<TimeTableDetail> findAllByTeacherAndSchoolClass(Teacher teacher, SchoolClass schoolClass);
}
