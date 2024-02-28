package phongvan.hischoolbackend.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import phongvan.hischoolbackend.entity.SchoolYear;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SemesterRequest {
    private Integer id;
    @NotBlank
    private String name;
    private SchoolYear schoolYear;

}
