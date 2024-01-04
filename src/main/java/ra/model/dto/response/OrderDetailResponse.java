package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public class OrderDetailResponse {
        private Long id;
        private Integer quantity;
        private Double unitPrice;
        private String userName;
        private String productName;
    }
