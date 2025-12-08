package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User findByUsername(String username);
    void createUser(String firstName, String lastName, int age,
                    String email, String password, List<Long> roleIds,
                    RedirectAttributes redirectAttributes);
    void updateUser(Long id, String firstName, String lastName, int age,
                    String email, String password, List<Long> roleIds,
                    RedirectAttributes redirectAttributes);
    void deleteUser(Long id, RedirectAttributes redirectAttributes);
    User saveUser(User user);
}