package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.OrderStatus;
import ra.model.entity.Orders;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Long> {
    List<Orders> findByStatus(OrderStatus orderStatus);
    List<Orders> findByUsersId(Long userId);
}
