package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.SchoolYear;

import java.util.List;
import java.util.Optional;

@Repository
public interface YearRepository extends JpaRepository<SchoolYear, Integer> {
    Optional<SchoolYear> findByName(String name);

    Boolean existsByNameAndSchool_Id(String name, Integer id);

    Page<SchoolYear> findByNameContainingIgnoreCase(String name, Pageable pageable);

    SchoolYear findFirstByOrderByIdDesc();

    List<SchoolYear> findAllBySchool_Id(Integer id, Sort by);

    Optional<SchoolYear> findByNameAndSchool_Id(String yearName, Integer schoolId);

}
