package ra.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductRequest {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName;
    @NotBlank(message = "Mô tả không được để trống")
    @Pattern(regexp = ".{10,}",message = "Phải có ít nhất 10 kí tự")
    @Column(columnDefinition = "TEXT")
    private String description;
    @NotBlank(message = "Giá sản phẩm không được để trống")
    private Double unitPrice;
    @NotBlank(message = "Số lượng nhập vào không được để trống")
    private Integer stock;
    @NotBlank(message = "Ngày thêm sản phẩm không được để trống")
    private Date created_at;
    private Boolean status;
    @NotBlank(message = "Danh mục không được để trống")
    private Long categoryId;
    private MultipartFile image;
}
