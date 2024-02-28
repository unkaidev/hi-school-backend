package phongvan.hischoolbackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.IssuedPlace;

@Repository
public interface IssuedPlaceRepository extends JpaRepository<IssuedPlace, Integer> {
}
