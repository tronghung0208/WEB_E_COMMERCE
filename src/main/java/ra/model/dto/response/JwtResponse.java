package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.entity.Users;

import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtResponse {
    private String fullName;
    private String token;
    private final String type = "Bearer";

    private Set<String> roles;
}
