package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.School;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {
    Optional<School> findByName(String name);

    Boolean existsByName(String name);

    Page<School> findByNameContainingIgnoreCase(String name, Pageable pageable);

    School findFirstByOrderByIdDesc();

    List<School> findAllByCreatedAtBetween(Date time, Date time1);

    School findFirstByOrderByCreatedAtDesc();

    @Query("SELECT MONTH(s.createdAt), COUNT(s) FROM School s WHERE YEAR(s.createdAt) = :year GROUP BY MONTH(s.createdAt)")
    List<Object[]> countSchoolsByMonth(@Param("year") int year);
}
