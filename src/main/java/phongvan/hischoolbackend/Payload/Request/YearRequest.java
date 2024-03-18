package phongvan.hischoolbackend.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearRequest {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String schoolId;
}
