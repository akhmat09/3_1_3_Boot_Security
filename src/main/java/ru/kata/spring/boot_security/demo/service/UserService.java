package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.entity.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User findByUsername(String username);
    void saveUser(User user);
    void updateUser(Long id, User user);
    void deleteUser(Long id);
    void saveUserWithRoles(User user, List<Long> roleIds);
    void updateUserWithRoles(Long id, User user, List<Long> roleIds);
    User createUserFromDto(UserDto userDto);
    User updateUserFromDto(Long id, UserDto userDto);
}