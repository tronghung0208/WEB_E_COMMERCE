package ra.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.exception.CustomException;
import ra.model.dto.request.UserRequestLogin;
import ra.model.dto.request.UserRequestRegister;
import ra.service.auth.AuthService;

@RestController
@RequestMapping("/api.myservice.com/v1/auth")

public class AuthController {
    @Autowired
    private AuthService usersService;
    @PostMapping("/sign-in")
    public ResponseEntity<?> handleLogin(@Valid @RequestBody  UserRequestLogin requestLogin) throws CustomException {
            return new ResponseEntity<>(usersService.handleLogin(requestLogin), HttpStatus.OK);
    }
    @PostMapping("/sign-up")
    public ResponseEntity<String> handleRegister(@Valid @RequestBody UserRequestRegister requestRegister) throws CustomException {
        return new ResponseEntity<>(usersService.handleRegister(requestRegister),HttpStatus.OK);
    }
}
