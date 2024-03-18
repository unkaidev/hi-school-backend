package phongvan.hischoolbackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.Parent;
@Repository
public interface ParentRepository extends JpaRepository<Parent,Integer> {
}
