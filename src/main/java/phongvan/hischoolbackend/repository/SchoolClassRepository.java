package phongvan.hischoolbackend.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.*;
import phongvan.hischoolbackend.entity.SchoolClass;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass,Integer> {
    Optional<SchoolClass> findByName(String name);

    Boolean existsByName(String name);

    Page<SchoolClass> findByNameContainingIgnoreCase(String name, Pageable pageable);

    SchoolClass findFirstByOrderByIdDesc();

    List<SchoolClass> findAllByName(String schoolClassName);

    List<SchoolClass> findAllBySchoolYear(SchoolYear schoolYear, Sort id);

    boolean existsByNameAndSchoolYear_Id(String schoolClassName, Integer id);

    boolean existsBySchoolYearAndTeacher(SchoolYear schoolYear, Teacher teacher);

    List<SchoolClass> findAllBySchoolYear_Id(int yearId, Sort id);

    List<SchoolClass> findAllBySchoolYear_IdAndGrade(int yearId, String grade, Sort id);

}
