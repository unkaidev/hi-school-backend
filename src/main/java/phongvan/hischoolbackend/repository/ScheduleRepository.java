package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.Schedule;
import phongvan.hischoolbackend.entity.Semester;
import phongvan.hischoolbackend.entity.Subject;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Optional<Schedule> findByName(String name);

    Boolean existsByName(String name);

    Page<Schedule> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Schedule findFirstByOrderByIdDesc();
    List<Schedule> findAllByName(String scheduleName);
    Collection<Schedule> findAllBySemester(Semester semester, Sort id);

    List<Schedule> findAllBySemester_Id(int semesterId, Sort id);
}
