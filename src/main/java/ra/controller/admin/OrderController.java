package ra.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.exception.CustomException;
import ra.model.dto.request.OrderAddRequest;
import ra.model.dto.response.CheckOutResponse;
import ra.model.dto.response.OrderDetailResponse;
import ra.model.entity.Orders;
import ra.service.user.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PutMapping("/order/update-status-confirm/{id}")
    public ResponseEntity<CheckOutResponse> updateWAITINGToCONFIRM(@PathVariable("id") Long orderId) throws CustomException {
        return new ResponseEntity<>(orderService.updateStatusWAITINGToCONFIRM(orderId),HttpStatus.OK);
    }

    @PutMapping("/order/update-status-delivery/{id}")
    public ResponseEntity<CheckOutResponse> updateCONFIRMToDELIVERY(@PathVariable("id") Long orderId) throws CustomException {
        return new ResponseEntity<>(orderService.updateStatusCONFIRMToDELIVERY(orderId),HttpStatus.OK);
    }
    @PutMapping("/order/update-status-success/{id}")
    public ResponseEntity<CheckOutResponse> updateDELIVERYToSUCCESS(@PathVariable("id") Long orderId) throws CustomException {
        return new ResponseEntity<>(orderService.updateStatusDELIVERYToSUCCESS(orderId),HttpStatus.OK);
    }
    @PutMapping("/order/update-status-denied/{id}")
    public ResponseEntity<CheckOutResponse> updateWAITINGorCONFIRMToDENIED(@PathVariable("id") Long orderId) throws CustomException {
        return new ResponseEntity<>(orderService.updateStatusWAITINGorCONFIRMToDENIED(orderId),HttpStatus.OK);
    }

    @PostMapping("/order/search-status")
    public ResponseEntity<List<Orders>> findByStatus(@RequestParam String status) throws CustomException {
        return new ResponseEntity<>(orderService.findByStatus(status),HttpStatus.OK);
    }

    @GetMapping("/all-order/history")
    public ResponseEntity<List<CheckOutResponse>> getAllOrderByUserId(Authentication authentication) throws CustomException {
        return new ResponseEntity<>(orderService.getAllOrder(authentication), HttpStatus.OK);
    }

}
