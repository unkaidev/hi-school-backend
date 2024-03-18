package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.RoleRepository;
import phongvan.hischoolbackend.entity.ERole;
import phongvan.hischoolbackend.entity.Role;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    public RoleRepository roleRepository;

    public List<Role> showRoleList() {
        return roleRepository.findAll();
    }
    public List<Role> getRolesByNameNotIn(List<ERole> names) {
        return roleRepository.findByNameNotIn(names);
    }
    public Role findRoleByName (ERole name){
        return roleRepository.findByName(name).get();
    }
}

