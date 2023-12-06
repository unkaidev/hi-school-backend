package phongvan.hischoolbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phongvan.hischoolbackend.entity.Group;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group,Integer> {
    Optional<Group> findByName(String name);
    Group getGroupByName(String name);

}
