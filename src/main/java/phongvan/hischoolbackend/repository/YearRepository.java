package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.School;
import phongvan.hischoolbackend.entity.SchoolYear;

import java.time.Year;
import java.util.Optional;

@Repository
public interface YearRepository extends JpaRepository<SchoolYear, Integer> {
    Optional<SchoolYear> findByName(String name);

    Boolean existsByName(String name);

    Page<SchoolYear> findByNameContainingIgnoreCase(String name, Pageable pageable);

    SchoolYear findFirstByOrderByIdDesc();
}
