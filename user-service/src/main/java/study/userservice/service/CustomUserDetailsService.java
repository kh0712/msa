package study.userservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import study.userservice.domain.User;
import study.userservice.domain.UserRepository;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if(user == null){
            throw new UsernameNotFoundException(username);
        }


        return new org.springframework.security.core
                .userdetails.User(  user.getEmail(),
                                    user.getEncryptedPwd(),
                                    new ArrayList<>());
    }
}
