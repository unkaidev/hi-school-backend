package phongvan.hischoolbackend.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import phongvan.hischoolbackend.entity.Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRequest {
    private Integer id;
    private String avatar;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String dateOfBirth;
    private String gender;
    @NotBlank
    private String nationality;
    @NotBlank
    private String ethnicity;
    @NotBlank
    private String citizenId;
    @NotBlank
    private String issuedDate;
    @NotBlank
    private IssuedPlaceRequest issuedPlace;
    @NotBlank
    private Address permanentAddress;
    @NotBlank
    private Address contactAddress;
    @NotBlank
    private UserRequest user;
    @NotBlank
    private String firstWorkDate;
    @NotBlank
    private String group;


}
