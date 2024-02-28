package phongvan.hischoolbackend.Payload.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageResponse {
    private int EC;
    private String EM;
    private Object DT;

}
