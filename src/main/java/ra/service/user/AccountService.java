package ra.service.user;

import org.springframework.security.core.Authentication;
import ra.exception.CustomException;
import ra.model.dto.request.UpdatePasswordRequest;
import ra.model.dto.request.UserUpdateRequest;
import ra.model.entity.Users;

public interface AccountService {
    void update(Authentication authentication, UserUpdateRequest userUpdateRequest);
    void updatePassword(Authentication authentication, UpdatePasswordRequest updatePasswordRequest) throws CustomException;

}
