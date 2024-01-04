package ra.service.auth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.exception.CustomException;
import ra.model.dto.request.UserRequestLogin;
import ra.model.dto.request.UserRequestRegister;
import ra.model.dto.response.JwtResponse;
import ra.model.entity.Roles;
import ra.model.entity.Users;
import ra.repository.RolesRepository;
import ra.repository.UsersRepository;
import ra.security.jwt.JwtProvider;
import ra.security.user_principle.UserPrinciple;
import ra.service.auth.AuthService;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public JwtResponse handleLogin(UserRequestLogin requestLogin) throws CustomException {
        Optional<Users> optionalUsers = usersRepository.findByEmail(requestLogin.getEmail());

        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            if (passwordEncoder.matches(requestLogin.getPassword(), users.getPassword())) {
                users.setLoginAttempts(0);
                usersRepository.save(users);

                if (users.getStatus()) {
                    try {
                        Authentication authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword()));
                        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
                        String token = jwtProvider.generateToken(userPrinciple);
                        return JwtResponse.builder()
                                .fullName(userPrinciple.getUsers().getFullName())
                                .token(token)
                                .roles(userPrinciple.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toSet()))
                                .build();
                    } catch (AuthenticationException a) {
                        throw new CustomException("Lỗi xác thực");
                    }
                } else {
                    throw new CustomException("Tài khoản của bạn đã bị khóa");
                }
            } else {
                users.setLoginAttempts(users.getLoginAttempts() + 1);
                usersRepository.save(users);

                if (users.getLoginAttempts() >= 3&&!isAdmin(users)) {
                    users.setStatus(false);
                    usersRepository.save(users);
                    throw new CustomException("Tài khoản đã bị khóa do đăng nhập sai quá nhiều lần");
                }

                throw new CustomException("Mật khẩu không đúng");
            }
        }

        throw new CustomException("Tài khoản không tồn tại");
    }

    private boolean isAdmin(Users users) {
        return users.getRoles().stream().map(Roles::getName).toList().contains("ROLE_ADMIN");
    }
    @Override
    public String handleRegister(UserRequestRegister requestRegister) throws CustomException {
        Optional<Users> optionalUsers = usersRepository.findByEmail(requestRegister.getEmail());
        if (optionalUsers.isPresent()) {
            throw new CustomException("Email đã được sử dụng");
        }

        Set<Roles> roles = new HashSet<>();
        requestRegister.getRoles().forEach(item -> {
            roles.add(rolesRepository.findByName(item).orElseThrow(() -> new RuntimeException(item + "not found")));
        });
        Users user = Users.builder()
                .email(requestRegister.getEmail())
                .fullName(requestRegister.getFullName())
                .password(passwordEncoder.encode(requestRegister.getPassword()))
                .phone(requestRegister.getPhone())
                .status(true).roles(roles)
                .created_at(new Date()).build();
        usersRepository.save(user);
        return "success";
    }


}
