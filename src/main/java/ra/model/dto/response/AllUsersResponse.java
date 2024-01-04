package ra.model.dto.response;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AllUsersResponse {
    private String fullName;
    private String email;
    private Boolean status;
    private String phone;
    private Date created_at;
}
