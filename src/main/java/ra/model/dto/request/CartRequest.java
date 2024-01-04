package ra.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartRequest {
    @NotBlank(message = "Số lượng không được trống")
    private Integer quantity;
    @NotBlank(message = "ID sản phẩm không được để trống")
    private Long productId;
}
