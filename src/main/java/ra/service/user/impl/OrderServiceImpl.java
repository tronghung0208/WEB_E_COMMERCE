package ra.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.exception.CustomException;
import ra.model.dto.request.OrderAddRequest;
import ra.model.dto.response.CheckOutResponse;
import ra.model.dto.response.OrderDetailResponse;
import ra.model.entity.Cart;
import ra.model.entity.OrderDetail;
import ra.model.entity.OrderStatus;
import ra.model.entity.Orders;
import ra.repository.CartRepository;
import ra.repository.OrderDetailRepository;
import ra.repository.OrderRepository;
import ra.security.user_principle.UserPrinciple;
import ra.service.user.OrderService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public Orders addNewOrderToUserByIdUser(OrderAddRequest addRequest, Authentication authentication) throws CustomException {
        if (authentication != null && authentication.isAuthenticated()) {
            UserPrinciple userLogin = (UserPrinciple) authentication.getPrincipal();
            List<Cart> cartList = cartRepository.findAllByUsersId(userLogin.getUsers().getId());
            Orders orders = orderRepository.save(getOrders(addRequest, cartList, userLogin));
            return orders;
        } else {
            throw new CustomException("Xin mời đăng nhập");
        }
    }

    // Phương thức lấy ra order
    private Orders getOrders(OrderAddRequest addRequest, List<Cart> cartList, UserPrinciple userLogin) {
        Double totalPrice = 0.0;
        for (Cart c : cartList
        ) {
            totalPrice = totalPrice + c.getQuantity() * c.getProduct().getUnitPrice();
        }
        Orders newOrder = new Orders();
        newOrder.setTotalPrice(totalPrice);
        newOrder.setStatus(OrderStatus.WAITING);
        newOrder.setAddress(addRequest.getAddress());
        newOrder.setNote(addRequest.getNote());
        newOrder.setPhone(addRequest.getPhone());
        newOrder.setCreated_at(new Date());
        newOrder.setReceived_at(getDateAfterSevenDays());
        newOrder.setUsers(userLogin.getUsers());
        return newOrder;
    }

    // Phương thức cộng ngày kể từ khi khởi tạo đơn hàng
    private Date getDateAfterSevenDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Đặt lịch cho ngày và giờ hiện tại
        calendar.add(Calendar.DAY_OF_MONTH, 7); // Thêm 7 ngày
        return calendar.getTime();
    }

    @Override
    public List<CheckOutResponse> getAllOrderByUserId(Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            List<Orders> ordersList = orderRepository.findByUsersId(userPrinciple.getUsers().getId());
            List<CheckOutResponse> checkOutResponses = ordersList.stream()
                    .map(this::convertCheckOutResponse)
                    .toList();
            return checkOutResponses;
        }
        return null;
    }

    @Override
    public List<Orders> findByStatus(String status) {
        List<Orders> orders = new ArrayList<>();

        switch (status.toUpperCase()) {
            case "SUCCESS":
                orders = orderRepository.findByStatus(OrderStatus.SUCCESS);
                break;
            case "CONFIRM":
                orders = orderRepository.findByStatus(OrderStatus.CONFIRM);
                break;
            case "DELIVERY":
                orders = orderRepository.findByStatus(OrderStatus.DELIVERY);
                break;
            case "CANCEL":
                orders = orderRepository.findByStatus(OrderStatus.CANCEL);
                break;
            case "WAITING":
                orders = orderRepository.findByStatus(OrderStatus.WAITING);
                break;
            default:
                break;
        }
        return orders;
    }

    private List<OrderDetailResponse> getAllOderDetail(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrdersId(orderId);
        return orderDetails.stream()
                .map(orderDetail -> OrderDetailResponse.builder()
                        .id(orderDetail.getId())
                        .quantity(orderDetail.getQuantity())
                        .unitPrice(orderDetail.getUnitPrice())
                        .userName(orderDetail.getOrders().getUsers().getFullName())
                        .productName(orderDetail.getProduct().getProductName())
                        .build())
                .collect(Collectors.toList());
    }

    private CheckOutResponse convertCheckOutResponse(Orders orders) {
        return CheckOutResponse.builder()
                .serialNumber(orders.getSerialNumber())
                .totalPrice(orders.getTotalPrice())
                .note(orders.getNote())
                .phone(orders.getPhone())
                .status(orders.getStatus())
                .address(orders.getAddress())
                .created_at(orders.getCreated_at())
                .received_at(orders.getReceived_at())
                .orderDetailResponses(getAllOderDetail(orders.getId()))
                .build();
    }

    @Override
    public CheckOutResponse updateStatusWAITINGToCONFIRM(Long id) throws CustomException {
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        if (optionalOrders.isPresent()) {
            Orders orders = optionalOrders.get();
            if (orders.getStatus().equals(OrderStatus.WAITING)) {
                orders.setNote("Đơn hàng của bạn đã được xác nhận,shop đang chuẩn bị hàng");
                orders.setStatus(OrderStatus.CONFIRM);
                orderRepository.save(orders);
                return convertCheckOutResponse(orders);
            } else {
                throw new CustomException("Đơn hàng đang ở trạng thái " + orders.getStatus());
            }
        }
        throw new CustomException("Đơn hàng chưa có");
    }

    @Override
    public CheckOutResponse updateStatusCONFIRMToDELIVERY(Long id) throws CustomException {
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        if (optionalOrders.isPresent()) {
            Orders orders = optionalOrders.get();
            if (orders.getStatus().equals(OrderStatus.CONFIRM)) {
                orders.setNote("Đơn đang được giao đến bạn trong vòng 7 ngày");
                orders.setStatus(OrderStatus.DELIVERY);
                orderRepository.save(orders);
                return convertCheckOutResponse(orders);
            } else {
                throw new CustomException("Đơn hàng đang ở trạng thái " + orders.getStatus());
            }
        }
        throw new CustomException("Đơn hàng chưa có");
    }

    @Override
    public CheckOutResponse updateStatusDELIVERYToSUCCESS(Long id) throws CustomException {
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        if (optionalOrders.isPresent()) {
            Orders orders = optionalOrders.get();
            if (orders.getStatus().equals(OrderStatus.DELIVERY)) {
                orders.setNote("Đơn được giao thành công");
                orders.setStatus(OrderStatus.SUCCESS);
                orderRepository.save(orders);
                return convertCheckOutResponse(orders);
            } else {
                throw new CustomException("Đơn hàng đang ở trạng thái " + orders.getStatus());
            }
        }
        throw new CustomException("Đơn hàng chưa có");
    }

    @Override
    public CheckOutResponse updateStatusWAITINGToCANCEL(Long id) throws CustomException {
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        if (optionalOrders.isPresent()) {
            Orders orders = optionalOrders.get();
            if (orders.getStatus().equals(OrderStatus.WAITING)) {
                orders.setNote("Đơn hàng đã bị hủy");
                orders.setStatus(OrderStatus.CANCEL);
                orderRepository.save(orders);
                return convertCheckOutResponse(orders);
            } else {
                throw new CustomException("Đơn hàng đang ở trạng thái " + orders.getStatus() + " bạn không thể " + OrderStatus.CANCEL);
            }
        }
        throw new CustomException("Đơn hàng chưa có");
    }

    @Override
    public CheckOutResponse updateStatusWAITINGorCONFIRMToDENIED(Long id) throws CustomException {
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        if (optionalOrders.isPresent()) {
            Orders orders = optionalOrders.get();
            if (orders.getStatus().equals(OrderStatus.WAITING) || orders.getStatus().equals(OrderStatus.CONFIRM)) {
                orders.setNote("Đơn hàng của bạn bị từ chối, liên hệ Admin để biết thêm");
                orders.setStatus(OrderStatus.DENIED);
                orderRepository.save(orders);
                return convertCheckOutResponse(orders);
            } else {
                throw new CustomException("Đơn hàng đang ở trạng thái " + orders.getStatus());
            }
        }
        throw new CustomException("Đơn hàng chưa có");
    }

    @Override
    public List<CheckOutResponse> getAllOrder(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            List<Orders> ordersList = orderRepository.findAll();
            List<CheckOutResponse> checkOutResponses = ordersList.stream()
                    .map(this::convertCheckOutResponse)
                    .toList();
            return checkOutResponses;
        }
        return null;
    }

}
