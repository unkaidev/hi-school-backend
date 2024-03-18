package phongvan.hischoolbackend.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.Semester;
import phongvan.hischoolbackend.entity.Teacher;
import phongvan.hischoolbackend.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Optional<Teacher> findByLastName(String lastName);

    Boolean existsByLastName(String lastName);

    Page<Teacher> findByLastNameContainingIgnoreCase(String name, Pageable pageable);

    Teacher findFirstByOrderByIdDesc();

    Teacher findByUser(User user);
    List<Teacher> findAllByTimeTableDetailsNotEmpty();

    List<Teacher> findAllByGroup(String group);

}
