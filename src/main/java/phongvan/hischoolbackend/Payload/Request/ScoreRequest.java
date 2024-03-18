package phongvan.hischoolbackend.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreRequest {
    private Integer id;

    @NotBlank
    private Double dailyScore;

    @NotBlank
    private Double midtermScore;

    @NotBlank
    private Double finalScore;

    @NotBlank
    private String subjectEvaluation;

}
