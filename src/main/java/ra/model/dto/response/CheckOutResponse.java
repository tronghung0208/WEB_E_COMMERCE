package ra.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.entity.OrderDetail;
import ra.model.entity.OrderStatus;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CheckOutResponse {
    private String serialNumber;
    private Double totalPrice;
    private String note;
    private String phone;
    private OrderStatus status;
    private String address;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date created_at;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date received_at;
    private List<OrderDetailResponse> orderDetailResponses;
}
