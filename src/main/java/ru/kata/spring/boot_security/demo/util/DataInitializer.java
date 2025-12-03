package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {
        if (roleService.findByName("ROLE_ADMIN") == null) {
            roleService.saveRole(new Role("ROLE_ADMIN"));
        }
        if (roleService.findByName("ROLE_USER") == null) {
            roleService.saveRole(new Role("ROLE_USER"));
        }
    }

    private void initializeUsers() {
        if (userService.findByUsername("admin@mail.ru") == null) {
            User admin = new User();
            admin.setEmail("admin@mail.ru");
            admin.setPassword("admin");
            admin.setFirstName("Admin");
            admin.setLastName("Adminov");
            admin.setAge(35);

            Role adminRole = roleService.findByName("ROLE_ADMIN");
            Role userRole = roleService.findByName("ROLE_USER");
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminRoles.add(userRole);
            admin.setRoles(adminRoles);

            userService.saveUser(admin);
        }

        if (userService.findByUsername("user@mail.ru") == null) {
            User user = new User();
            user.setEmail("user@mail.ru");
            user.setPassword("user");
            user.setFirstName("User");
            user.setLastName("Userov");
            user.setAge(30);

            Role userRole = roleService.findByName("ROLE_USER");
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            user.setRoles(userRoles);

            userService.saveUser(user);
        }
    }
}