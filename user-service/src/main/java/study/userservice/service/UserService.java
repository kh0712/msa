package study.userservice.service;

import study.userservice.domain.User;
import study.userservice.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto dto);
    UserDto getUserByUserId(String userId);
    List<User> getUserByAll();
}
