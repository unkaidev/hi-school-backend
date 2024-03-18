package phongvan.hischoolbackend.Payload.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import phongvan.hischoolbackend.entity.Address;
import phongvan.hischoolbackend.entity.IssuedPlace;
import phongvan.hischoolbackend.entity.Parent;
import phongvan.hischoolbackend.entity.User;


import java.io.File;
import java.sql.Blob;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
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
    private ParentRequest parent;



}
