package ra.service.user.impl;

import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.exception.CustomException;
import ra.model.dto.request.CartRequest;
import ra.model.dto.request.OrderAddRequest;
import ra.model.dto.response.CartResponse;
import ra.model.dto.response.CheckOutResponse;
import ra.model.dto.response.OrderDetailResponse;
import ra.model.dto.response.ProductResponse;
import ra.model.entity.Cart;
import ra.model.entity.OrderDetail;
import ra.model.entity.Orders;
import ra.model.entity.Product;
import ra.repository.CartRepository;
import ra.repository.OrderDetailRepository;
import ra.repository.ProductRepository;
import ra.security.user_principle.UserPrinciple;
import ra.service.admin.ProductService;
import ra.service.admin.impl.ProductServiceImpl;
import ra.service.user.CartService;
import ra.service.user.OrderDetailService;
import ra.service.user.OrderService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductServiceImpl productServiceImpl;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public Cart addToCart(CartRequest cartRequest, Authentication authentication) throws CustomException {

        if (cartRequest.getQuantity() > 0) {
            if (authentication != null && authentication.isAuthenticated()) {
                UserPrinciple userLogin = (UserPrinciple) authentication.getPrincipal();
                Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow(() -> new CustomException("product not found"));
                Cart existCart = cartRepository.findCartsByProductIdAndUsersId(cartRequest.getProductId(), userLogin.getUsers().getId());
                if (cartRequest.getQuantity()>product.getStock()){
                    throw new CustomException("Số lượng sản phẩm trong kho không đủ");
                }
                if (existCart != null) {
                    Integer newQuantity = existCart.getQuantity() + cartRequest.getQuantity();
                    existCart.setQuantity(newQuantity);
                    return cartRepository.save(existCart);
                } else {
                    Cart cartItem = Cart.builder()
                            .quantity(cartRequest.getQuantity())
                            .product(product)
                            .users(userLogin.getUsers())
                            .build();
                    return cartRepository.save(cartItem);
                }

            }
        } else {
            throw new CustomException("Số lượng phải lớn hơn 0");
        }

        return null;
    }

    @Override
    public List<CartResponse> getAllCart(Authentication authentication) throws CustomException {
        UserPrinciple userLogin = (UserPrinciple) authentication.getPrincipal();
        if (userLogin != null) {
            List<Cart> cartList = cartRepository.findAllByUsersId(userLogin.getUsers().getId());
            List<CartResponse> list = cartList.stream().map(item -> CartResponse.builder()
                    .cartId(item.getId())
                    .quantity(item.getQuantity())
                    .productResponse(productServiceImpl.convertToProductResponse(item.getProduct()))
                    .userId(userLogin.getUsers().getId())
                    .fullNameUser(userLogin.getUsers().getFullName())
                    .build()).toList();
            return list;
        }
        throw new CustomException("Xin mời đăng nhập");
    }


    @Override
    public CartResponse getCartsById(Long cartId, Authentication authentication) throws CustomException {
        UserPrinciple userLogin = (UserPrinciple) authentication.getPrincipal();

        Cart cart = cartRepository.findCartsByIdAndUsersId(cartId, userLogin.getUsers().getId());
        if (cart == null) {
            throw new CustomException("Bạn chưa có sản phẩm này trong giỏ hàng");
        } else {
            return CartResponse.builder()
                    .cartId(cart.getId())
                    .quantity(cart.getQuantity())
                    .productResponse(productServiceImpl.convertToProductResponse(cart.getProduct()))
                    .userId(cart.getUsers().getId())
                    .fullNameUser((cart.getUsers().getFullName()))
                    .build();
        }

    }

    @Override
    public Cart updateCartQuantity(Integer quantity, Long cartId, Authentication authentication) throws CustomException {
        if (quantity > 0) {
            UserPrinciple userLogin = (UserPrinciple) authentication.getPrincipal();
            if (userLogin != null) {
                Optional<Cart> optionalCart = cartRepository.findById(cartId);
                if (optionalCart.isEmpty()) {
                    throw new CustomException("Sản phẩm không có trong giỏ hàng");
                } else {
                    Cart cartUpdate = optionalCart.get();
                    cartUpdate.setQuantity(quantity);
                    return cartRepository.save(cartUpdate);
                }
            }
            throw new CustomException("Xin mời đăng nhập");
        } else {
            throw new CustomException("Số lượng phải lớn hơn 0");
        }
    }

    @Override
    public void deleteCart(Long cartId, Authentication authentication) throws CustomException {
        UserPrinciple userLogin = (UserPrinciple) authentication.getPrincipal();
        if (userLogin != null) {
            cartRepository.deleteById(cartId);
        } else {
            throw new CustomException("Không thể xóa cart");
        }
    }

    @Override
    public void deleteAllCartByUserId(Authentication authentication) {
        UserPrinciple userLogin = (UserPrinciple) authentication.getPrincipal();
        cartRepository.deleteAllByUsersId(userLogin.getUsers().getId());
    }

    @Override
    public CheckOutResponse checkOut(Authentication authentication, OrderAddRequest addRequest) throws CustomException {
        Orders orders = orderService.addNewOrderToUserByIdUser(addRequest, authentication);
      List<OrderDetail> orderDetails=  orderDetailService.addNewOrderDetail(orders,authentication);
        List<OrderDetailResponse> orderDetailResponses = orderDetails.stream()
                .map(orderDetail -> OrderDetailResponse.builder()
                        .id(orderDetail.getId())
                        .quantity(orderDetail.getQuantity())
                        .unitPrice(orderDetail.getUnitPrice())
                        .userName(orderDetail.getOrders().getUsers().getFullName())
                        .productName(orderDetail.getProduct().getProductName())
                        .build())
                .collect(Collectors.toList());
        deleteAllCartByUserId(authentication);
        return CheckOutResponse.builder()
                .serialNumber(orders.getSerialNumber())
                .totalPrice(orders.getTotalPrice())
                .note(orders.getNote())
                .phone(orders.getPhone())
                .status(orders.getStatus())
                .address(orders.getAddress())
                .created_at(orders.getCreated_at())
                .received_at(orders.getReceived_at())
                .orderDetailResponses(orderDetailResponses)
                .build();
    }


}
