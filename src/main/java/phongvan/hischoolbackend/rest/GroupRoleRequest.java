package phongvan.hischoolbackend.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupRoleRequest {
    Integer groupId;
    Integer roleId;
}
