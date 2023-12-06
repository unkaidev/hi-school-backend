package phongvan.hischoolbackend.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import phongvan.hischoolbackend.entity.Group;
import phongvan.hischoolbackend.entity.Role;

import phongvan.hischoolbackend.repository.GroupRepository;
import phongvan.hischoolbackend.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoleApiService {

    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;

    public Response getAllRole() {
        try {
            List<Role> roles = roleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            if (roles.isEmpty()) {
                return Response.builder()
                        .EC(-1)
                        .EM("NOT FOUND ANY ROLE")
                        .DT(null)
                        .build();
            }
            return Response.builder()
                    .EC(0)
                    .EM("GET ALL ROLE SUCCESS")
                    .DT(roles)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .EC(-1)
                    .EM("SOMETHING WENT WRONG IN SERVER")
                    .DT(null)
                    .build();
        }
    }

    public Response createNewRole(@RequestBody Collection<Role> roleList) {
        try {
            Collection<Role> roles = new ArrayList<>();
            for (Role role : roleList) {
                String roleName = role.getName();
                Role roleExists = roleRepository.findByName(roleName);
                if (roleExists == null) {
                    roles.add(role);
                }
            }

            if (roles.isEmpty()) {
                return Response.builder()
                        .EC(0)
                        .EM("NOTHING TO CREATE")
                        .DT(null)
                        .build();
            }
            roleRepository.saveAllAndFlush(roles);

            return Response.builder()
                    .EC(0)
                    .EM("CREATE ROLE SUCCESS: " + roles.size() + " ROLES")
                    .DT(null)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .EC(-1)
                    .EM("SOMETHING WENT WRONG IN SERVER")
                    .DT(null)
                    .build();
        }
    }

    public Response getRoleByGroup(int groupId) {
        try {
            Group group = groupRepository.getById(groupId);
            Collection<Role> roles = group.getRoles();
            if (roles.isEmpty()) {
                return Response.builder()
                        .EC(-1)
                        .EM("NOT FOUND ANY ROLE")
                        .DT(null)
                        .build();
            }
            return Response.builder()
                    .EC(0)
                    .EM("GET ALL ROLE SUCCESS")
                    .DT(roles)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .EC(-1)
                    .EM("SOMETHING WENT WRONG IN SERVER")
                    .DT(null)
                    .build();
        }
    }

    public Response assignRoleToGroup(Map<String, AssignRoleRequest> request) {
        try {
            AssignRoleRequest roleRequest = request.get("data");
            Integer groupId = Integer.valueOf(roleRequest.getGroupId());
            Group group = groupRepository.getById(groupId);
            group.setRoles(null);
            Collection<Role> roles = new ArrayList<>();
            for (int i = 0; i < roleRequest.getGroupRoles().length; i++) {
                GroupRoleRequest groupRoleRequest = roleRequest.getGroupRoles()[i];
                Integer roleIndex = groupRoleRequest.getRoleId();
                Role roleGroup = roleRepository.getById(roleIndex);
                roles.add(roleGroup);
            }
            group.setRoles(roles);
            groupRepository.saveAndFlush(group);
            return Response.builder()
                    .EC(0)
                    .EM("ASSIGN ROLE TO GROUP SUCCESS")
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .EC(-1)
                    .EM("SOMETHING WENT WRONG IN SERVER")
                    .build();
        }
    }


    public Response deleteRole(Map<String, Integer> request) {
        try {

            Role roleToDelete = roleRepository.getById(request.get("id"));

            Collection<Group> groups = groupRepository.findAll();
            for (Group group : groups) {
                Collection<Role> roleGroup = group.getRoles();
                roleGroup.remove(roleToDelete);
            }
            roleRepository.delete(roleToDelete);
            return Response.builder()
                    .EC(0)
                    .EM("DELETE ROLE SUCCESS")
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .EC(-1)
                    .EM("SOMETHING WENT WRONG IN SERVER")
                    .build();
        }
    }
}
