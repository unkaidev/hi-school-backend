package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.ERole;
import phongvan.hischoolbackend.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    List<User> findAllByRoles_Name(ERole roleName, Sort id);

    List<User> findAllByRoles_NameNotInAndSchoolId(List<ERole> roleNames, Integer id, Sort by);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);

    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    User findFirstByOrderByIdDesc();

    List<User> findAllByRoles_NameAndSchool_Id(ERole eRole, Integer schoolId, Sort id);
}
