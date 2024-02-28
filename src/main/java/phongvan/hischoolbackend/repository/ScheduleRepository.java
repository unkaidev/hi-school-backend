package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.Schedule;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Optional<Schedule> findByScheduleName(String name);

    Boolean existsByScheduleName(String name);

    Page<Schedule> findByScheduleNameContainingIgnoreCase(String name, Pageable pageable);

    Schedule findFirstByOrderByIdDesc();
}
