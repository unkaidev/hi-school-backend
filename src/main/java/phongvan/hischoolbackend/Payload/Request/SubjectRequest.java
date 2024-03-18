package phongvan.hischoolbackend.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import phongvan.hischoolbackend.entity.SchoolYear;
import phongvan.hischoolbackend.entity.Semester;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequest {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String grade;
    private SemesterRequest semester;

}
