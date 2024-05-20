package phongvan.hischoolbackend.Repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.SchoolYear;
import phongvan.hischoolbackend.entity.Student;
import phongvan.hischoolbackend.entity.Transcript;

import java.util.Collection;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Integer> {
    Transcript findByStudentAndSchoolYear(Student student, SchoolYear schoolYear);

    Collection<? extends Transcript> findAllBySchoolYear(SchoolYear schoolYear, Sort id);
}
