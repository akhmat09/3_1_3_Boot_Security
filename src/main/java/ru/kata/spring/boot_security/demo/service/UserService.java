package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User findByUsername(String username);
    User saveUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
}