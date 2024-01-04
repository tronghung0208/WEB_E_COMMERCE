package ra.service.user;

import org.springframework.security.core.Authentication;
import ra.model.entity.OrderDetail;
import ra.model.entity.Orders;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> addNewOrderDetail(Orders orders,Authentication authentication);
}
