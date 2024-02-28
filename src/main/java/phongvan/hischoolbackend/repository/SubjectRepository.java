package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.Subject;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Optional<Subject> findByName(String name);

    Boolean existsByName(String name);

    Page<Subject> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Subject findFirstByOrderByIdDesc();

    List<Subject> findAllByName(String subjectName);
}
