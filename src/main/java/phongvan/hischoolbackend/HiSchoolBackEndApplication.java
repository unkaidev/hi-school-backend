package phongvan.hischoolbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import phongvan.hischoolbackend.Repository.AddressRepository;
import phongvan.hischoolbackend.Repository.IssuedPlaceRepository;
import phongvan.hischoolbackend.Repository.RoleRepository;
import phongvan.hischoolbackend.entity.Address;
import phongvan.hischoolbackend.entity.ERole;
import phongvan.hischoolbackend.entity.IssuedPlace;
import phongvan.hischoolbackend.entity.Role;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HiSchoolBackEndApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HiSchoolBackEndApplication.class, args);
        initializeRoles(context);
        initializeIssuedPlace(context);
    }

    private static void initializeRoles(ConfigurableApplicationContext context) {
        RoleRepository roleRepository = context.getBean(RoleRepository.class);
        if (roleRepository.count() == 0) {
            List<Role> roles = Arrays.asList(
                    new Role(1, ERole.ROLE_ADMIN),
                    new Role(2, ERole.ROLE_MANAGER),
                    new Role(3, ERole.ROLE_HEADTEACHER),
                    new Role(4, ERole.ROLE_TEACHER),
                    new Role(5, ERole.ROLE_USER));

            roleRepository.saveAll(roles);
        }
    }

    private static void initializeIssuedPlace(ConfigurableApplicationContext context) {
        IssuedPlaceRepository issuedPlaceRepository = context.getBean(IssuedPlaceRepository.class);
        if (issuedPlaceRepository.count() == 0) {
            AddressRepository addressRepository = context.getBean(AddressRepository.class);
            Address address = new Address(1, "90 P. Nguyễn Du", "Trần Hưng Đạo", "Hoàn Kiếm", "Hà Nội", "Hà Nội");
            addressRepository.save(address);
            List<IssuedPlace> issuedPlaces = Arrays.asList(
                    new IssuedPlace(1, " Cục Cảnh sát quản lý hành chính về trật tự xã hội", address));
            issuedPlaceRepository.saveAll(issuedPlaces);
        }
    }

}
