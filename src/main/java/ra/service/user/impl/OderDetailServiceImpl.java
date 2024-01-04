package ra.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.model.dto.request.OrderAddRequest;
import ra.model.entity.Cart;
import ra.model.entity.OrderDetail;
import ra.model.entity.Orders;
import ra.repository.CartRepository;
import ra.repository.OrderDetailRepository;
import ra.repository.OrderRepository;
import ra.security.user_principle.UserPrinciple;
import ra.service.user.OrderDetailService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartRepository cartRepository;
    @Override
    public List<OrderDetail> addNewOrderDetail( Orders orders,Authentication authentication) {
        UserPrinciple userPrinciple= (UserPrinciple) authentication.getPrincipal();
        List<Cart> cartList=cartRepository.findAllByUsersId(userPrinciple.getUsers().getId());
        List<OrderDetail> orderDetails = cartList.stream()
                .map(cart -> OrderDetail.builder()
                        .quantity(cart.getQuantity())
                        .unitPrice(cart.getProduct().getUnitPrice())
                        .orders(orders)
                        .product(cart.getProduct())
                        .build())
                .collect(Collectors.toList());
        return orderDetailRepository.saveAll(orderDetails);
    }
}

















