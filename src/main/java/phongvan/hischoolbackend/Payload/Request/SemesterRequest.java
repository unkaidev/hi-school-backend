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
    @NotBlank
    private String study_period;
    private String start_date;
    private String end_date;
    @NotBlank
    private YearRequest schoolYear;
}
