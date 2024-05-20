package phongvan.hischoolbackend.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.ERole;
import phongvan.hischoolbackend.entity.School;
import phongvan.hischoolbackend.entity.SchoolClass;
import phongvan.hischoolbackend.entity.User;

import java.util.Date;
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

    List<User> findAllBySchool_Id(Integer id);
    List<User> findAllByRoles_NameAndCreatedAtBetween(ERole eRole, Date time, Date time1, Sort id);

    List<User> findAllBySchool_IdAndCreatedAtBetween(Integer id, Date time, Date time1);

    List<User> findAllByCreatedAtBetween(Date time, Date time1);

    @Query("SELECT MONTH(s.createdAt), COUNT(s) FROM User s WHERE YEAR(s.createdAt) = :year GROUP BY MONTH(s.createdAt)")
    List<Object[]> countUsersByMonth(@Param("year") int year);

    @Query("SELECT YEAR(u.createdAt), COUNT(u) FROM User u WHERE u.school.id = :schoolId GROUP BY YEAR(u.createdAt)")
    List<Object[]> countUsersInSchoolByYear(@Param("schoolId") int schoolId);

    List<User> findAllByRoles_NameAndSchool_IdAndCreatedAtBetween(ERole eRole, Integer id, Date time, Date time1);
}
