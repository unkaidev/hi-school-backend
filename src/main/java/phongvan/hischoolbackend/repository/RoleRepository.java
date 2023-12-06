package phongvan.hischoolbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phongvan.hischoolbackend.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Role findByName(String roleName);
}
