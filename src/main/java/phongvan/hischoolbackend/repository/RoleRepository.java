package phongvan.hischoolbackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.ERole;
import phongvan.hischoolbackend.entity.Role;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    Optional<Role> findByName(ERole name);

}
