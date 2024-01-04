package ra.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.exception.CustomException;
import ra.model.dto.request.CartRequest;
import ra.model.dto.request.OrderAddRequest;
import ra.model.dto.response.CartResponse;
import ra.model.entity.Cart;
import ra.service.user.CartService;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/user")
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/add-cart")
    public ResponseEntity<Cart> postCart(@RequestParam("productId") Long productId,
                                         @RequestParam("quantity") Integer quantity, Authentication authentication) throws CustomException {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(productId);
        cartRequest.setQuantity(quantity);
        Cart cartItem = cartService.addToCart(cartRequest, authentication);
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }
    @GetMapping("/get-cart")
    public ResponseEntity <List<CartResponse>> getCart(Authentication authentication) throws CustomException {
        return new ResponseEntity<>(cartService.getAllCart(authentication),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, Authentication authentication) {
        try {
            cartService.deleteCart(id, authentication);
            return ResponseEntity.ok().build(); // Return 200 OK if deletion is successful
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting cart item: " + e.getMessage());

        }
    }
    @PutMapping("/update-quantity")
    public ResponseEntity<?> updateQuantity(@RequestParam("quantity") Integer quantity,
                                            @RequestParam("cartId") Long cartId,
                                            Authentication authentication) throws CustomException {
        return new ResponseEntity<>(cartService.updateCartQuantity(quantity,cartId,authentication),HttpStatus.OK);
    }
    @GetMapping("/get-cart/{id}")
    public ResponseEntity<?> getCartById(@PathVariable("id") Long cartId,Authentication authentication) throws CustomException {
        return new ResponseEntity<>(cartService.getCartsById(cartId,authentication),HttpStatus.OK);
    }
    @PostMapping("/checkout")
    public ResponseEntity<?> addOrder(@RequestBody OrderAddRequest orderAddRequest, Authentication authentication) throws CustomException {
        return new ResponseEntity<>(cartService.checkOut(authentication,orderAddRequest), HttpStatus.OK);
    }
}
