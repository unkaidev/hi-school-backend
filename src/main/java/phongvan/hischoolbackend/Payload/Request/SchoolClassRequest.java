package phongvan.hischoolbackend.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolClassRequest {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String grade;
    @NotBlank
    private YearRequest schoolYear;
    @NotBlank
    private TeacherRequest teacher;

    private List<StudentRequest> students;

}
