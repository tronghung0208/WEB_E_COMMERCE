package ra.service.user.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.exception.CustomException;
import ra.model.dto.request.UpdatePasswordRequest;
import ra.model.dto.request.UserUpdateRequest;
import ra.model.entity.Users;
import ra.repository.UsersRepository;
import ra.security.user_principle.UserPrinciple;
import ra.service.user.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void update(Authentication authentication, UserUpdateRequest userUpdateRequest) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            Users users = userPrinciple.getUsers();
            users.setFullName(userUpdateRequest.getFullName());
            users.setPhone(userUpdateRequest.getPhone());
            usersRepository.save(users);
        }
    }

    @Override
    public void updatePassword(Authentication authentication, UpdatePasswordRequest updatePasswordRequest) throws CustomException {
        if (authentication != null && authentication.isAuthenticated()) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            Users users = userPrinciple.getUsers();
            String password = users.getPassword();
            if (passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), password) && updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())) {
                users.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
                usersRepository.save(users);
            }else {
                throw new CustomException("Mật khẩu chưa đúng");
            }
        }
    }

}
