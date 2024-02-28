package phongvan.hischoolbackend.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import phongvan.hischoolbackend.entity.Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolRequest {
    private Integer id;
    @NotBlank
    private String name;

    private Address address;


}