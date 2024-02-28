package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.Semester;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    Optional<Semester> findByName(String name);

    Boolean existsByName(String name);

    Page<Semester> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Semester findFirstByOrderByIdDesc();

    List<Semester> findAllByName(String semesterName);
}
