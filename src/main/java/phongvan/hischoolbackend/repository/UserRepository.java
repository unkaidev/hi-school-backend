package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.User;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);

    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    User findFirstByOrderByIdDesc();
}
