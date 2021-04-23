package study.userservice.domain;


import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;
import study.userservice.dto.UserDto;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 100,unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String pwd;

    @Column(nullable = false, length = 100,unique = true)
    private String userId;

    @Column(nullable = false)
    private String encryptedPwd;

    /**
     * 회원가입시 dto로 User 엔티티를 만든다.
     *
     */

    public static User of(UserDto dto){
        return User.builder()
                    .email(dto.getEmail())
                    .name(dto.getName())
                    .pwd(dto.getPwd())
                    .encryptedPwd(dto.getEncryptedPwd())
                    .userId(dto.getUserId())
                    .build();
    }

    @Builder
    private User(Long id, String email, String name, String pwd, String userId, String encryptedPwd) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.pwd = pwd;
        this.userId = userId;
        this.encryptedPwd = encryptedPwd;
    }
}
