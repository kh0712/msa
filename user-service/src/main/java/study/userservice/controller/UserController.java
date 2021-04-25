package study.userservice.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.userservice.domain.User;
import study.userservice.dto.UserDto;
import study.userservice.service.UserService;
import study.userservice.vo.Greeting;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final Environment env;
    private final Greeting greeting;
    private final UserService userService;

    /**
     * 전체 유저 조회
     */
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){

        List<User> users = userService.getUserByAll();
        List<ResponseUser> result = users.stream()
                                    .map(ResponseUser::of)
                                    .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * userId로 단건조회
     */

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") String userId){

        UserDto findUser = userService.getUserByUserId(userId);
        ResponseUser result = ResponseUser.of(findUser);

        return ResponseEntity.ok(result);
    }

    /**
     * 유저 생성
     * Sign Up dto : name, email, pwd
     */

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequestDto dto){
        UserDto userDto = UserDto.of(dto);

        UserDto result = userService.createUser(userDto);

        SignUpResponseDto responseDto = SignUpResponseDto.of(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/health-check")
    public String status(){
        return "It is working in user service PORT: " + env.getProperty("local.server.port");
    }

    @GetMapping("/welcome")
    public String welcome(){
        return greeting.getMessage();
    }


    /**
     * DTOs
     */

    @Data
    public static class SignUpRequestDto {

        @Email(message = "Not Valid Email")
        private String email;

        @Size(min = 2, message = "name not be less than 2 char")
        @NotNull(message = "name cannot be null")
        private String name;

        @NotNull
        @Size(min = 8,message = "password not be less than 8 char")
        private String pwd;

    }

    @Data
    public static class SignUpResponseDto {

        private String email;

        private String name;

        private String userId;

        @Builder
        private SignUpResponseDto(String email, String name, String userId) {
            this.email = email;
            this.name = name;
            this.userId = userId;
        }

        public static SignUpResponseDto of(UserDto dto){
            return SignUpResponseDto.builder()
                    .userId(dto.getUserId())
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .build();
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResponseUser{
        private String email;
        private String name;
        private String userId;

        private List<ResponseOrder> orders;

        @Builder
        public ResponseUser(String email, String name, String userId, List<ResponseOrder> orders) {
            this.email = email;
            this.name = name;
            this.userId = userId;
            this.orders = orders;
        }

        public static ResponseUser of(User user){
            return ResponseUser.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .userId(user.getUserId())
                    .orders(new ArrayList<>())
                    .build();
        }

        public static ResponseUser of(UserDto user){
            return ResponseUser.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .userId(user.getUserId())
                    .orders(user.getOrders())
                    .build();
        }


    }

    @Data
    public static class ResponseOrder{
        private String productId;
        private Integer qty;
        private Integer unitPrice;
        private Integer totalPrice;
        private LocalDateTime createdAt;

        private String orderId;


    }
}
