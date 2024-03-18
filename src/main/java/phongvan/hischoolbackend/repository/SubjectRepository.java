package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.Semester;
import phongvan.hischoolbackend.entity.Subject;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Optional<Subject> findByName(String name);

    Boolean existsByName(String name);

    Page<Subject> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Subject findFirstByOrderByIdDesc();

    List<Subject> findAllByName(String subjectName);

    Collection<Subject> findAllBySemester(Semester semester, Sort id);

    List<Subject> findAllBySemester_Id(int semesterId, Sort id);

    List<Subject> findAllBySemester_IdAndGrade(int semesterId, String grade, Sort id);
}
