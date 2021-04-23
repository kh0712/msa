package study.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.userservice.controller.UserController;
import study.userservice.domain.User;
import study.userservice.domain.UserRepository;
import study.userservice.dto.UserDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static study.userservice.controller.UserController.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto dto) {
        dto.setEncryptedPwd(passwordEncoder.encode(dto.getPwd()));
        dto.setUserId(UUID.randomUUID().toString());
        User user = User.of(dto);

        userRepository.save(user);
        return dto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        User findUser = userRepository.findByUserId(userId);
        UserDto userDto = UserDto.of(findUser);

        //TODO Order Service 에서 해당 유저의 주문 목록을 가져와야한다.
        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);

        return userDto;
    }

    @Override
    public List<User> getUserByAll() {
        return userRepository.findAll();
    }

}
