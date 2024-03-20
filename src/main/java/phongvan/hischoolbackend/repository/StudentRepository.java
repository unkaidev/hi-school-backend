package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.School;
import phongvan.hischoolbackend.entity.Student;
import phongvan.hischoolbackend.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByLastName(String lastName);

    Boolean existsByLastName(String lastName);

    Page<Student> findByLastNameContainingIgnoreCase(String name, Pageable pageable);

    Student findFirstByOrderByIdDesc();

    Student findByUser(User user);


}
