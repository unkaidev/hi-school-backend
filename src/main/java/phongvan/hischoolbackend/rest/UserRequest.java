package phongvan.hischoolbackend.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    Integer id;
    String email;
    String phone;
    String firstname;
    String lastname;
    String password;
    String address;
    String gender;
    String groupId;
}
