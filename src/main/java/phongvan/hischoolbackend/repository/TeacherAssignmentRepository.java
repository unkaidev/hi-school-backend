package phongvan.hischoolbackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.*;

import java.util.List;

@Repository
public interface TeacherAssignmentRepository extends JpaRepository<TeacherAssignment, Integer> {
    void deleteAllByTeacherAndSubject(Teacher teacherInTTD, Subject subjectInTTD);

    List<TeacherAssignment> findAllBySemester(Semester semester);

    List<TeacherAssignment> findAllBySemester_Id(int semesterId);
}
