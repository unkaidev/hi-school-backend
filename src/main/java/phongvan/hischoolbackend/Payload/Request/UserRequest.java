package phongvan.hischoolbackend.Payload.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import phongvan.hischoolbackend.entity.SchoolClass;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private Integer id;
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    private String roleId;
    private String phone;
    @NotBlank
    private String gender;
    @NotBlank
    private String password;
    @NotBlank
    private boolean active;

    private SchoolClassRequest school;
    private Integer schoolId;
}