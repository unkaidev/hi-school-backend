package phongvan.hischoolbackend.Payload.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private String id;
    private String username;
    private String phone;
    private String email;
    private String schoolId;
    private List<String> roles;
    private int EC;
    private String EM;

    public UserInfoResponse(String id, String username, String phone, String email, List<String> roles, int EC, String EM) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.roles = roles;
        this.EC = EC;
        this.EM = EM;
    }
}
