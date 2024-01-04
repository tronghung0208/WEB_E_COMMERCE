package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "serial_number", length = 100, nullable = false, unique = true)
    private String serialNumber=generateSerialNumber();
    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String note;
    private String phone;
    private String address;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date created_at;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date received_at;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;
    private String generateSerialNumber() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
