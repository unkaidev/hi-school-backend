package phongvan.hischoolbackend.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.entity.Group;
import phongvan.hischoolbackend.repository.GroupRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupApiService {
    private final GroupRepository groupRepository;

    public Response getGroupMemberList() {
        try {
            List<Group> groups = groupRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            if (groups.isEmpty()) {
                return Response.builder()
                        .EC(-1)
                        .EM("NOT FOUND ANY GROUP")
                        .DT(null)
                        .build();
            } else {
                return Response.builder()
                        .EC(0)
                        .EM("GET GROUP SUCCESS")
                        .DT(groups)
                        .build();
            }
        } catch (Exception e) {
            return Response.builder()
                    .EC(-1)
                    .EM("SOMETHING WENT WRONG IN SERVER")
                    .DT(null)
                    .build();
        }
    }

    public Response createNewGroup(Map<String, String>[] groupList) {
        try {
            Collection<Group> groupsToAdd = new ArrayList<>();
            for (Map<String, String> groupMap : groupList) {
                String name = groupMap.get("name");
                String description = groupMap.get("description");
                Group groupExists = groupRepository.getGroupByName(name);
                if (groupExists == null) {
                    Group groupToAdd = Group.builder()
                            .name(name)
                            .description(description)
                            .build();
                    groupsToAdd.add(groupToAdd);
                }
            }

            if (groupsToAdd.isEmpty()) {
                return Response.builder()
                        .EC(0)
                        .EM("NOTHING TO CREATE")
                        .DT(null)
                        .build();
            }
            groupRepository.saveAllAndFlush(groupsToAdd);

            return Response.builder()
                    .EC(0)
                    .EM("CREATE GROUP SUCCESS: "
                            + groupsToAdd.size()
                            + " GROUPS")
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

    public Response deleteGroup(Map<String, Integer> request) {
        try {

            Group groupToDelete = groupRepository.getById(request.get("id"));
            groupRepository.delete(groupToDelete);
            return Response.builder()
                    .EC(0)
                    .EM("DELETE GROUP SUCCESS")
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .EC(-1)
                    .EM("SOMETHING WENT WRONG IN SERVER")
                    .build();
        }
    }

}
