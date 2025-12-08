package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAllWithRoles();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        return user;
    }

    @Override
    public void createUser(String firstName, String lastName, int age,
                           String email, String password, List<Long> roleIds,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setAge(age);

            Set<Role> roles = getRolesFromIds(roleIds);
            user.setRoles(roles);

            userRepository.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating user: " + e.getMessage());
        }
    }

    @Override
    public void updateUser(Long id, String firstName, String lastName, int age,
                           String email, String password, List<Long> roleIds,
                           RedirectAttributes redirectAttributes) {
        try {
            User existingUser = getUserById(id);
            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setAge(age);
            existingUser.setEmail(email);

            if (password != null && !password.trim().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(password));
            }

            if (roleIds != null && !roleIds.isEmpty()) {
                Set<Role> roles = getRolesFromIds(roleIds);
                existingUser.setRoles(roles);
            }

            userRepository.save(existingUser);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(Long id, RedirectAttributes redirectAttributes) {
        try {
            userRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
    }

    private Set<Role> getRolesFromIds(List<Long> roleIds) {
        Set<Role> roles = new HashSet<>();

        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                roleService.getAllRoles().stream()
                        .filter(r -> r.getId().equals(roleId))
                        .findFirst()
                        .ifPresent(roles::add);
            }
        }

        if (roles.isEmpty()) {
            Role defaultRole = roleService.findByName("ROLE_USER");
            if (defaultRole != null) {
                roles.add(defaultRole);
            }
        }

        return roles;
    }
    @Override
    public User saveUser(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }
}