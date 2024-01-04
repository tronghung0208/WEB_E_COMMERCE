package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "serial_number", length = 100, nullable = false, unique = true)
    private String serialNumber=generateUniqueSerialNumber();
    private String productName;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Double unitPrice;
    private Integer stock;
    private String image;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date created_at;
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    private String generateUniqueSerialNumber() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
