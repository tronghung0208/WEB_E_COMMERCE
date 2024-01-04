package ra.service.auth;

import ra.exception.CustomException;
import ra.model.dto.request.UserRequestLogin;
import ra.model.dto.request.UserRequestRegister;
import ra.model.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse handleLogin(UserRequestLogin requestLogin) throws CustomException;
    String handleRegister(UserRequestRegister requestRegister) throws CustomException;
}
