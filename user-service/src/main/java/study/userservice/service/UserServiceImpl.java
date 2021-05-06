package study.userservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import study.userservice.client.OrderServiceClient;
import study.userservice.controller.UserController;
import study.userservice.domain.User;
import study.userservice.domain.UserRepository;
import study.userservice.dto.UserDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static study.userservice.controller.UserController.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final Environment env;
    private final OrderServiceClient orderServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

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
//        List<ResponseOrder> orders = new ArrayList<>();

        /** Using Rest Template **/

//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> orderListResponse =
//                restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {
//                });
//        List<ResponseOrder> orderList = orderListResponse.getBody();

//        List<ResponseOrder> orderList = null;
//        try {
//            orderList = orderServiceClient.getOrders(userId);
//        } catch (FeignException e) {
//            log.error(e.getMessage());
//        }

//        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);

        log.info("Before call Order Micro Service");
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
        List<ResponseOrder> orderList = circuitbreaker.run(() -> orderServiceClient.getOrders(userId),
                throwable -> new ArrayList<>());
        log.info("After call Order Micro Service");



        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public List<User> getUserByAll() {
        return userRepository.findAll();
    }

}
