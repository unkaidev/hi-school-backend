package phongvan.hischoolbackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.Address;
@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {
}
