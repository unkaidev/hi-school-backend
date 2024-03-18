package phongvan.hischoolbackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.*;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {
    Score findByStudentAndSubject(Student student, Subject subject);

    void deleteAllByTeacherAndSubject(Teacher teacher, Subject subject);

    List<Score> findAllBySemesterAndStudent(Semester semester, Student student);

    List<Score> findAllBySemester(Semester semester);

    List<Score> findAllBySemesterAndStudentAndTeacher(Semester semester, Student student, Teacher teacher);
}
