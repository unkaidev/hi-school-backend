package phongvan.hischoolbackend.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.Semester;
import phongvan.hischoolbackend.entity.TimeTable;

import java.util.List;
import java.util.Set;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Integer> {

    List<TimeTable> findAllBySemester(Semester semesterFind);

    List<TimeTable> findAllBySemester_Id(int semesterId);

    List<TimeTable> findAllByStudyWeek(String studyWeek);

    List<TimeTable> findAllByStudyWeekAndStudyDay(String studyWeek, String studyDay);

    List<TimeTable> findAllByStudyDay(String studyDay);
}
