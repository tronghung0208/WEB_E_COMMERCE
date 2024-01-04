package ra.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.entity.Category;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductResponse {
    private Long id;
    private String productName;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String serialNumber;
    private Double unitPrice;
    private Integer stock;
    private String image;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date created_at;
    private Boolean status;
    private String categoryName;
}
