package ra.service.user;

import org.springframework.security.core.Authentication;
import ra.exception.CustomException;
import ra.model.dto.request.OrderAddRequest;
import ra.model.dto.response.CheckOutResponse;
import ra.model.entity.OrderStatus;
import ra.model.entity.Orders;

import java.util.List;

public interface OrderService {
    Orders addNewOrderToUserByIdUser(OrderAddRequest addRequest, Authentication authentication) throws CustomException;
    List<CheckOutResponse> getAllOrderByUserId(Authentication authentication);
    List<Orders> findByStatus(String status);

    CheckOutResponse updateStatusWAITINGToCONFIRM( Long id) throws CustomException;

    CheckOutResponse updateStatusCONFIRMToDELIVERY( Long id) throws CustomException;

    CheckOutResponse updateStatusDELIVERYToSUCCESS( Long id) throws CustomException;
    CheckOutResponse updateStatusWAITINGToCANCEL( Long id) throws CustomException;
    CheckOutResponse updateStatusWAITINGorCONFIRMToDENIED(Long id) throws CustomException;
    List<CheckOutResponse> getAllOrder(Authentication authentication);
}
