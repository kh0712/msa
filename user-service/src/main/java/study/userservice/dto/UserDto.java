package study.userservice.dto;

import lombok.*;
import study.userservice.controller.UserController;
import study.userservice.domain.User;

import java.util.Date;
import java.util.List;

import static study.userservice.controller.UserController.*;

@Getter
public class UserDto {

    //Sign Up dto
    private String email;
    private String name;
    private String pwd;

    //중간단계로 이동이될때 사용
    private String userId;
    private String encryptedPwd;

    private List<ResponseOrder> orders;


    @Builder
    private UserDto(String email, String name, String pwd, String userId, String encryptedPwd) {
        this.email = email;
        this.name = name;
        this.pwd = pwd;
        this.userId = userId;
        this.encryptedPwd = encryptedPwd;
    }

    /**
     * User 영속성 객체를 dto 로 만든다.
     */

    public static UserDto of(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .pwd(user.getPwd())
                .userId(user.getUserId())
                .encryptedPwd(user.getEncryptedPwd())
                .build();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEncryptedPwd(String encryptedPwd) {
        this.encryptedPwd = encryptedPwd;
    }

    public void setOrders(List<ResponseOrder> orders) {
        this.orders = orders;
    }

    public static UserDto of(SignUpRequestDto dto){
        return UserDto.builder()
                .email(dto.getEmail())
                .pwd(dto.getPwd())
                .name(dto.getName())
                .build();
    }
}
