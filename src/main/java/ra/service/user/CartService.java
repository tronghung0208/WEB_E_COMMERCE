package ra.service.user;

import org.springframework.security.core.Authentication;
import ra.exception.CustomException;
import ra.model.dto.request.CartRequest;
import ra.model.dto.request.OrderAddRequest;
import ra.model.dto.response.CartResponse;
import ra.model.dto.response.CheckOutResponse;
import ra.model.entity.Cart;

import java.util.List;

public interface CartService {
    Cart addToCart(CartRequest cartRequest, Authentication authentication) throws CustomException;
    List<CartResponse> getAllCart(Authentication authentication) throws CustomException;
    CartResponse getCartsById(Long cartId,Authentication authentication) throws CustomException;
    Cart updateCartQuantity(Integer quantity, Long cartId,Authentication authentication) throws CustomException;
    void deleteCart(Long cartId,Authentication authentication) throws CustomException;

    void deleteAllCartByUserId(Authentication authentication);

CheckOutResponse checkOut(Authentication authentication, OrderAddRequest orderAddRequest) throws CustomException;
}
