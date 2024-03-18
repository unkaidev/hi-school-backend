package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.School;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {
    Optional<School> findByName(String name);

    Boolean existsByName(String name);

    Page<School> findByNameContainingIgnoreCase(String name, Pageable pageable);

    School findFirstByOrderByIdDesc();
}
