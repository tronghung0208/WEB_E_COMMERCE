package ra.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.exception.CustomException;
import ra.model.dto.response.CheckOutResponse;
import ra.service.user.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/user")
public class OrderControllerUser {
    @Autowired
    private OrderService orderService;
    @PutMapping("/order/update-status-cancer/{id}")
    public ResponseEntity<CheckOutResponse> updateCONFIRMToDELIVERY1(@PathVariable("id") Long orderId) throws CustomException {
        return new ResponseEntity<>(orderService.updateStatusWAITINGToCANCEL(orderId), HttpStatus.OK);
    }

    @GetMapping("/order/history")
    public ResponseEntity<List<CheckOutResponse>> getAllOrderByUserId(Authentication authentication) throws CustomException {
        return new ResponseEntity<>(orderService.getAllOrderByUserId(authentication), HttpStatus.OK);
    }
}
