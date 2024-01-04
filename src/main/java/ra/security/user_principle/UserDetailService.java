package ra.security.user_principle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ra.model.entity.Users;
import ra.repository.UsersRepository;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if(email.isEmpty()){
            throw new UsernameNotFoundException(email+" rong");

        }
        Optional<Users> optionalUsers = usersRepository.findByEmail(email);
        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
UserPrinciple userPrinciple=UserPrinciple.builder()
        .users(users)
        .authorities(users.getRoles().stream().map(item->new SimpleGrantedAuthority(item.getName())).toList())
        .build();
            return userPrinciple;
        }else {
        throw new UsernameNotFoundException(email+" not found");
    }

    }

}
