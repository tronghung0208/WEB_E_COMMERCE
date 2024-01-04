package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartResponse {
    private Long cartId;
    private Integer quantity;
    private ProductResponse productResponse;
    private Long userId;
    private String fullNameUser;
}
