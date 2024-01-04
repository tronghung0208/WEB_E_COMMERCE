package ra.controller.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.exception.CustomException;
import ra.model.dto.request.UpdatePasswordRequest;
import ra.model.dto.request.UserUpdateRequest;
import ra.service.user.AccountService;

@RestController
@RequestMapping("/api.myservice.com/v1/user")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody UserUpdateRequest userUpdateRequest, Authentication authentication){
        accountService.update(authentication,userUpdateRequest);
        return new ResponseEntity<>("Chỉnh sửa thông tin thành công", HttpStatus.CREATED);
    }
@PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest,Authentication authentication) throws CustomException {
       accountService.updatePassword(authentication,updatePasswordRequest);
        return new ResponseEntity<>("Thay đổi mật khẩu thành công",HttpStatus.CREATED);
}
}
